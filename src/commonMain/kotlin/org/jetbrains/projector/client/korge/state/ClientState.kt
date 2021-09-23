package org.jetbrains.projector.client.korge.state

import com.soywiz.klogger.Logger
import com.soywiz.korio.net.ws.WebSocketClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mainStage
import org.jetbrains.projector.client.common.misc.ImageCacher
import org.jetbrains.projector.client.common.misc.ParamsProvider
import org.jetbrains.projector.client.common.misc.TimeStamp
import org.jetbrains.projector.client.common.protocol.KotlinxJsonToClientHandshakeDecoder
import org.jetbrains.projector.client.common.protocol.KotlinxJsonToServerHandshakeEncoder
import org.jetbrains.projector.client.korge.ServerEventsProcessor
import org.jetbrains.projector.client.korge.WindowSizeController
import org.jetbrains.projector.client.korge.misc.ClientStats
import org.jetbrains.projector.client.korge.misc.PingStatistics
import org.jetbrains.projector.client.korge.protocol.SupportedTypesProvider
import org.jetbrains.projector.client.korge.window.OnScreenMessenger
import org.jetbrains.projector.client.korge.window.WindowDataEventsProcessor
import org.jetbrains.projector.client.korge.window.WindowHeader
import org.jetbrains.projector.client.korge.window.WindowManager
import org.jetbrains.projector.common.misc.Do
import org.jetbrains.projector.common.misc.toString
import org.jetbrains.projector.common.protocol.MessageDecoder
import org.jetbrains.projector.common.protocol.MessageEncoder
import org.jetbrains.projector.common.protocol.compress.MessageCompressor
import org.jetbrains.projector.common.protocol.compress.MessageDecompressor
import org.jetbrains.projector.common.protocol.handshake.*
import org.jetbrains.projector.common.protocol.toClient.ServerDrawCommandsEvent
import org.jetbrains.projector.common.protocol.toClient.ToClientMessageDecoder
import org.jetbrains.projector.common.protocol.toServer.*
import kotlin.math.roundToInt

sealed class ClientState {

  open suspend fun consume(action: ClientAction): ClientState {
    logger.error { "${this::class.simpleName}: can't consume ${action::class.simpleName}" }
    return this
  }

  private class AppLayers(
    val reconnectionMessageUpdater: (String?) -> Unit,
  )

  object UninitializedPage : ClientState() {

    private suspend fun configureWebPage(url: String): AppLayers {
      WindowHeader.initIcons()

//      document.body!!.apply {
//        style.apply {
//          backgroundColor = ParamsProvider.BACKGROUND_COLOR
//          asDynamic().overscrollBehaviorX = "none"
//          asDynamic().overscrollBehaviorY = "none"
//          asDynamic().touchAction = "none"
//        }
//
//        oncontextmenu = { false }
//      }
//
//      val reloadingMessageLayer = (document.createElement("div") as HTMLDivElement).apply {
//        id = "reloading-message-layer"
//        style.apply {
//          position = "absolute"
//          zIndex = "1"
//          margin = "0px"
//          width = "100%"
//          height = "100%"
//          top = "0px"
//          left = "0px"
//          asDynamic().pointerEvents = "none"
//        }
//
//        document.body!!.appendChild(this)
//      }
//
//      val reconnectionMessageUpdater = { newMessage: String? ->
//        val reconnectionMessage = createElement(ReconnectionMessage::class.js, jsObject<ReconnectionMessageProps> {
//          this.message = newMessage
//        })
//        render(reconnectionMessage, reloadingMessageLayer)
//      }
//
//      reconnectionMessageUpdater(null)
//
      OnScreenMessenger.showText("Starting connection", "Waiting for response from $url...", canReload = false)

//      if (!(window.asDynamic().isSecureContext as Boolean) && ParamsProvider.SHOW_NOT_SECURE_WARNING) {
//        window.alert(buildString {
//          append("Warning: You have opened this page in a not secure context. ")
//          append("This means that the browser will restrict usage of some features such as the clipboard access. ")
//          append("You can find the full list here: ")
//          append("https://developer.mozilla.org/en-US/docs/Web/Security/Secure_Contexts/features_restricted_to_secure_contexts. ")
//          appendLine("To make the context secure, please use HTTPS for the web page or host it locally.")
//          append("To disable this warning, please add `notSecureWarning=false` query parameter.")
//        })
//      }
//
//      return AppLayers(
//        reconnectionMessageUpdater = reconnectionMessageUpdater,
//      )

      return AppLayers { }  // todo: rewrite to korge
    }

    override suspend fun consume(action: ClientAction) = when (action) {
      is ClientAction.Start -> {
        val layers = configureWebPage(action.url)

        val webSocket = createWebSocketConnection(action.url, action.stateMachine)
          ?: object : WebSocketClient("${action.url} (bad url)", emptyList(), false) {}

        WaitingOpening(
          stateMachine = action.stateMachine,
          webSocket = webSocket,
          windowSizeController = action.windowSizeController,
          layers = layers,
        )
      }

      else -> super.consume(action)
    }
  }

  private class WaitingOpening(
    private val stateMachine: ClientStateMachine,
    private val webSocket: WebSocketClient,
    private val windowSizeController: WindowSizeController,
    private val layers: AppLayers,
    private val onHandshakeFinish: () -> Unit = {},
  ) : ClientState() {

    override suspend fun consume(action: ClientAction) = when (action) {
      is ClientAction.WebSocket.Open -> {
        OnScreenMessenger.showText("Connection is opened", "Sending handshake...", canReload = false)

        ClientStats.resetStats(action.openingTimeStamp.toDouble())

        val handshakeEvent = with(SupportedTypesProvider) {
          ToServerHandshakeEvent(
            commonVersion = COMMON_VERSION,
            commonVersionId = commonVersionList.indexOf(COMMON_VERSION),
            token = ParamsProvider.HANDSHAKE_TOKEN,
            displays = listOf(
              DisplayDescription(
                0,
                0,
                windowSizeController.currentSize.width,
                windowSizeController.currentSize.height,
                1.0
              )
            ),
            clientDoesWindowManagement = false,
            supportedToClientCompressions = supportedToClientDecompressors.map(MessageDecompressor<*>::compressionType),
            supportedToClientProtocols = supportedToClientDecoders.map(MessageDecoder<*, *>::protocolType),
            supportedToServerCompressions = supportedToServerCompressors.map(MessageCompressor<*>::compressionType),
            supportedToServerProtocols = supportedToServerEncoders.map(MessageEncoder<*, *>::protocolType)
          )
        }

        webSocket.send("$HANDSHAKE_VERSION;${handshakeVersionList.indexOf(HANDSHAKE_VERSION)}")
        webSocket.send(KotlinxJsonToServerHandshakeEncoder.encode(handshakeEvent))

        OnScreenMessenger.showText("Connection is opened", "Handshake is sent...", canReload = false)

        WaitingHandshakeReply(
          stateMachine = stateMachine,
          webSocket = webSocket,
          windowSizeController = windowSizeController,
          openingTimeStamp = action.openingTimeStamp,
          onHandshakeFinish = onHandshakeFinish,
          layers = layers,
        )
      }

      is ClientAction.WebSocket.Close -> {
        showDisconnectedMessage(webSocket.url, action)
        onHandshakeFinish()

        Disconnected
      }

      else -> super.consume(action)
    }
  }

  private class WaitingHandshakeReply(
    private val stateMachine: ClientStateMachine,
    private val webSocket: WebSocketClient,
    private val windowSizeController: WindowSizeController,
    private val openingTimeStamp: Int,
    private val onHandshakeFinish: () -> Unit,
    private val layers: AppLayers,
  ) : ClientState() {

    override suspend fun consume(action: ClientAction) = when (action) {
      is ClientAction.WebSocket.Message -> {
        val command = KotlinxJsonToClientHandshakeDecoder.decode(action.message)

        Do exhaustive when (command) {
          is ToClientHandshakeFailureEvent -> {
            OnScreenMessenger.showText("Handshake failure", "Reason: ${command.reason}", canReload = true)
            webSocket.close()
            onHandshakeFinish()

            Disconnected
          }

          is ToClientHandshakeSuccessEvent -> {
            command.colors?.let { ProjectorUI.setColors(it) }

//            FontFaceAppender.removeAppendedFonts()  // todo

            OnScreenMessenger.showText(
              "Loading fonts...",
              "0 of ${command.fontDataHolders.size} font(s) loaded",
              canReload = false
            )

            // todo
//            command.fontDataHolders.forEach { fontDataHolder ->
//              FontFaceAppender.appendFontFaceToPage(fontDataHolder.fontId, fontDataHolder.fontData) { loadedFontCount ->
//                if (loadedFontCount == command.fontDataHolders.size) {
//                  logger.info { "${command.fontDataHolders.size} font(s) loaded" }
//                  OnScreenMessenger.hide()
//
//                  stateMachine.fire(ClientAction.LoadAllFonts)
//                }
//                else {
//                  OnScreenMessenger.showText(
//                    "Loading fonts",
//                    "$loadedFontCount of ${command.fontDataHolders.size} font(s) loaded.",
//                    canReload = false
//                  )
//                }
//              }
//            }
            logger.info { "${command.fontDataHolders.size} font(s) loaded" }
            OnScreenMessenger.hide()
            stateMachine.fire(ClientAction.LoadAllFonts)

            LoadingFonts(
              stateMachine = stateMachine,
              webSocket = webSocket,
              windowSizeController = windowSizeController,
              openingTimeStamp = openingTimeStamp,
              encoder = SupportedTypesProvider.supportedToServerEncoders.first { it.protocolType == command.toServerProtocol },
              decoder = SupportedTypesProvider.supportedToClientDecoders.first { it.protocolType == command.toClientProtocol },
              decompressor = SupportedTypesProvider.supportedToClientDecompressors.first { it.compressionType == command.toClientCompression },
              compressor = SupportedTypesProvider.supportedToServerCompressors.first { it.compressionType == command.toServerCompression },
              onHandshakeFinish = onHandshakeFinish,
              layers = layers,
            )
          }
        }
      }

      is ClientAction.WebSocket.Close -> {
        showDisconnectedMessage(webSocket.url, action)
        onHandshakeFinish()

        Disconnected
      }

      else -> super.consume(action)
    }
  }

  private class LoadingFonts(
    private val stateMachine: ClientStateMachine,
    private val webSocket: WebSocketClient,
    private val windowSizeController: WindowSizeController,
    private val openingTimeStamp: Int,
    private val encoder: ToServerMessageEncoder,
    private val decoder: ToClientMessageDecoder,
    private val decompressor: MessageDecompressor<ByteArray>,
    private val compressor: MessageCompressor<String>,
    private val onHandshakeFinish: () -> Unit,
    private val layers: AppLayers,
  ) : ClientState() {

    override suspend fun consume(action: ClientAction) = when (action) {
      is ClientAction.LoadAllFonts -> {
        logger.debug { "All fonts are loaded. Ready to draw!" }

        webSocket.send("Unused string meaning fonts loading is done")  // todo: change this string
        onHandshakeFinish()

        ReadyToDraw(
          stateMachine = stateMachine,
          webSocket = webSocket,
          windowSizeController = windowSizeController,
          openingTimeStamp = openingTimeStamp,
          encoder = encoder,
          decoder = decoder,
          decompressor = decompressor,
          compressor = compressor,
          layers = layers,
          ImageCacher()
        )
      }

      else -> super.consume(action)
    }
  }

  private class ReadyToDraw(
    private val stateMachine: ClientStateMachine,
    private val webSocket: WebSocketClient,
    private val windowSizeController: WindowSizeController,
    openingTimeStamp: Int,
    private val encoder: ToServerMessageEncoder,
    private val decoder: ToClientMessageDecoder,
    private val decompressor: MessageDecompressor<ByteArray>,
    private val compressor: MessageCompressor<String>,
    private val layers: AppLayers,
    private val imageCacher: ImageCacher,
  ) : ClientState() {

    private val eventsToSend = mutableListOf<ClientEvent>(/*ClientSetKeymapEvent(nativeKeymap)*/)  // todo

    private val windowManager = WindowManager(stateMachine, imageCacher)

    private val windowDataEventsProcessor = WindowDataEventsProcessor(windowManager)

    private var drawPendingEvents = mainStage.launch {
      // redraw windows in case any missing images are loaded now
      while (true) {
        windowDataEventsProcessor.drawPendingEvents()
        delay(ParamsProvider.REPAINT_INTERVAL_MS.toLong())
      }
    }

    private val serverEventsProcessor = ServerEventsProcessor(windowDataEventsProcessor)

    private val messagingPolicy = (
            ParamsProvider.FLUSH_DELAY
              ?.let {
                MessagingPolicy.Buffered(
                  timeout = it,
                  isFlushNeeded = { eventsToSend.isNotEmpty() },
                  flush = { stateMachine.fire(ClientAction.Flush) }
                )
              }
              ?: MessagingPolicy.Unbuffered(
                isFlushNeeded = { eventsToSend.isNotEmpty() },
                flush = { stateMachine.fire(ClientAction.Flush) }
              )
            ).apply {
        onHandshakeFinished()
      }

//    private val sentReceivedBadgeShower: SentReceivedBadgeShower = if (ParamsProvider.SHOW_SENT_RECEIVED) {
//      DivSentReceivedBadgeShower()
//    }
//    else {
//      NoSentReceivedBadgeShower
//    }.apply {
//      onHandshakeFinished()
//    }  // todo

    private val pingStatistics = PingStatistics(
      openingTimeStamp = openingTimeStamp,
      requestPing = {
        stateMachine.fire(ClientAction.AddEvent(ClientRequestPingEvent(clientTimeStamp = TimeStamp.current.toInt() - openingTimeStamp)))
      }
    ).apply {
      onHandshakeFinished()
    }

//    private val inputController = InputController(
//      openingTimeStamp = openingTimeStamp,
//      stateMachine = stateMachine,
//      windowManager = windowManager,
//      windowPositionByIdGetter = windowManager::get,
//    ).apply {
//      addListeners()
//    }  // todo

//    private val typing = when (ParamsProvider.SPECULATIVE_TYPING) {
//      false -> Typing.NotSpeculativeTyping
//      true -> Typing.SpeculativeTyping(windowManager::getWindowCanvas)
//    }  // todo

//    private val markdownPanelManager = MarkdownPanelManager(windowManager::getWindowZIndex) { link ->
//      stateMachine.fire(ClientAction.AddEvent(ClientOpenLinkEvent(link)))
//    }  // todo

//    private val closeBlocker = when (ParamsProvider.BLOCK_CLOSING) {
//      true -> CloseBlockerImpl(window)
//      false -> NopCloseBlocker
//    }.apply {
//      setListener()
//    }  // todo

//    private val selectionBlocker = SelectionBlocker(window).apply {
//      blockSelection()
//    }  // todo

//    private val connectionWatcher = ConnectionWatcher { stateMachine.fire(ClientAction.WebSocket.NoReplies(it)) }.apply {
//      setWatcher()
//    }  // todo

    init {
//      windowSizeController.addListener()  // todo
    }

    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun consume(action: ClientAction) = when (action) {
      is ClientAction.WebSocket.Message -> {
//        connectionWatcher.resetTime()  // todo

        val receiveTimeStamp = TimeStamp.current
        val decompressed = decompressor.decompress(action.message)
        val decompressTimeStamp = TimeStamp.current
        val commands = decoder.decode(decompressed)
        val decodeTimestamp = TimeStamp.current
        serverEventsProcessor.process(
          commands,
          pingStatistics/*, typing, markdownPanelManager, inputController*/
        )  // todo
        val drawTimestamp = TimeStamp.current

        imageCacher.collectGarbage()

        eventsToSend.addAll(imageCacher.extractImagesToRequest())

        messagingPolicy.onToClientMessage()
//        sentReceivedBadgeShower.onToClientMessage()  // todo

        val drawEventCount = commands
          .filterIsInstance<ServerDrawCommandsEvent>()
          .map { it.drawEvents.size }
          .sum()

        val decompressingTimeMs = decompressTimeStamp - receiveTimeStamp
        val decodingTimeMs = decodeTimestamp - decompressTimeStamp
        val drawingTimeMs = drawTimestamp - decodeTimestamp

        ClientStats.toClientMessageSizeAverage.add(action.message.size.toLong())
        ClientStats.toClientMessageSizeRate.add(action.message.size.toLong())

        ClientStats.toClientCompressionRatioAverage.add(action.message.size.toDouble() / decompressed.size)

        ClientStats.drawEventCountAverage.add(drawEventCount.toLong())
        ClientStats.drawEventCountRate.add(drawEventCount.toLong())

        ClientStats.decompressingTimeMsAverage.add(decompressingTimeMs)
        ClientStats.decompressingTimeMsRate.add(decompressingTimeMs)

        ClientStats.decodingTimeMsAverage.add(decodingTimeMs)
        ClientStats.decodingTimeMsRate.add(decodingTimeMs)

        ClientStats.drawingTimeMsAverage.add(drawingTimeMs)
        ClientStats.drawingTimeMsRate.add(drawingTimeMs)

        val processTimestamp = TimeStamp.current

        val otherProcessingTimeMs = processTimestamp - drawTimestamp
        val totalTimeMs = processTimestamp - receiveTimeStamp

        ClientStats.otherProcessingTimeMsAverage.add(otherProcessingTimeMs)
        ClientStats.otherProcessingTimeMsRate.add(otherProcessingTimeMs)

        ClientStats.totalTimeMsAverage.add(totalTimeMs)
        ClientStats.totalTimeMsRate.add(totalTimeMs)

        if (ParamsProvider.SHOW_PROCESSING_TIME) {
          fun roundToTwoDecimals(number: Double) = number.toString(2)

          if (drawEventCount > 0) {
            logger.debug {
              "Timestamp is ${roundToTwoDecimals(processTimestamp)}:\t" +
                      "draw events count: $drawEventCount,\t" +
                      "decompressing: ${roundToTwoDecimals(decompressingTimeMs)} ms,\t\t" +
                      "decoding: ${roundToTwoDecimals(decodingTimeMs)} ms,\t\t" +
                      "drawing: ${roundToTwoDecimals(drawingTimeMs)} ms,\t\t" +
                      "other processing: ${roundToTwoDecimals(otherProcessingTimeMs)} ms,\t\t" +
                      "total: ${roundToTwoDecimals(totalTimeMs)} ms"
            }
          }
        }

        this
      }

      is ClientAction.AddEvent -> {
        val event = action.event

//        if (event is ClientKeyPressEvent) {  // todo
//          typing.addEventChar(event)
//        }

        eventsToSend.add(event)
        messagingPolicy.onAddEvent()

        this
      }

      is ClientAction.Flush -> {
        val message = encoder.encode(eventsToSend).also { eventsToSend.clear() }
        webSocket.send(compressor.compress(message))

//        sentReceivedBadgeShower.onToServerMessage()  // todo

        this
      }

      is ClientAction.WindowResize -> {
        serverEventsProcessor.onResized()

        this
      }

      is ClientAction.WebSocket.Close -> {
        Do exhaustive when (action.endedNormally) {
          true -> {
            logger.info { "Connection is closed..." }

            drawPendingEvents.cancel()
            pingStatistics.onClose()
            windowDataEventsProcessor.onClose()
            // todo:
//            inputController.removeListeners()
//            windowSizeController.removeListener()
//            typing.dispose()
//            markdownPanelManager.disposeAll()
//            closeBlocker.removeListener()
//            selectionBlocker.unblockSelection()
//            connectionWatcher.removeWatcher()

            showDisconnectedMessage(webSocket.url, action)
            Disconnected
          }

          false -> reloadConnection("Connection is closed unexpectedly, retrying the connection...")
        }
      }

      is ClientAction.WebSocket.NoReplies ->
        reloadConnection("No messages from server for ${action.elapsedTimeMs} ms, retrying the connection...")

      else -> super.consume(action)
    }

    private suspend fun reloadConnection(messageText: String): ClientState {
      logger.info { messageText }

      drawPendingEvents.cancel()
      pingStatistics.onClose()
//      inputController.removeListeners()  // todo
//      windowSizeController.removeListener()  // todo
//      typing.dispose()  // todo
//      connectionWatcher.removeWatcher()  // todo

      layers.reconnectionMessageUpdater(messageText)

      val newConnection = createWebSocketConnection(webSocket.url, stateMachine)
        ?: object : WebSocketClient("${webSocket.url} (bad url)", emptyList(), false) {}
      return WaitingOpening(stateMachine, newConnection, windowSizeController, layers) {
        windowDataEventsProcessor.onClose()
//        markdownPanelManager.disposeAll()  // todo
//        closeBlocker.removeListener()
//        selectionBlocker.unblockSelection()
        layers.reconnectionMessageUpdater(null)
      }
    }
  }

  object Disconnected : ClientState()

  companion object {

    private val logger = Logger<ClientState>()

    private const val NORMAL_CLOSURE_STATUS_CODE: Short = 1000
    private const val GOING_AWAY_STATUS_CODE: Short = 1001

    private val ClientAction.WebSocket.Close.endedNormally: Boolean
      get() = this.wasClean && this.code in setOf(NORMAL_CLOSURE_STATUS_CODE, GOING_AWAY_STATUS_CODE)

    private fun showDisconnectedMessage(url: String, action: ClientAction.WebSocket.Close) {
      val reason = action.reason.ifBlank { null }?.let { "Reason: $it" }
        ?: "The server hasn't reported a reason of the disconnection."

      Do exhaustive when (action.endedNormally) {
        true -> OnScreenMessenger.showText(
          "Disconnected",
          "It seems that your connection is ended normally. $reason",
          canReload = true
        )

        false -> OnScreenMessenger.showText(
          "Connection problem",
          "There is no connection to <strong>$url</strong>. " +
                  "The browser console can contain the error and a more detailed description. " +
                  "Everything we know is that <code>CloseEvent.code=${action.code}</code>, " +
                  "<code>CloseEvent.wasClean=${action.wasClean}</code>. $reason",
          canReload = true
        )
      }
    }

    private suspend fun createWebSocketConnection(url: String, stateMachine: ClientStateMachine): WebSocketClient? {
      return try {
        WebSocketClient(url).apply {
          onOpen {
            stateMachine.fire(ClientAction.WebSocket.Open(openingTimeStamp = TimeStamp.current.roundToInt()))
          }

          onClose { event ->
            stateMachine.fire(
              ClientAction.WebSocket.Close(
                wasClean = event.wasClean,
                code = event.code.toShort(),
                reason = event.message.orEmpty(),
              )
            )
          }

          onBinaryMessage { message ->
            stateMachine.fire(ClientAction.WebSocket.Message(message = message))
          }
        }
      } catch (t: Throwable) {
        stateMachine.fire(
          ClientAction.WebSocket.Close(
            wasClean = false,
            code = -1,
            reason = t.message ?: "got error during connection: $t",
          )
        )
        null
      }
    }
  }
}
