package org.jetbrains.projector.client.korge

import com.soywiz.klogger.Logger
import org.jetbrains.projector.client.korge.misc.PingStatistics
import org.jetbrains.projector.client.korge.state.ProjectorUI
import org.jetbrains.projector.client.korge.window.OnScreenMessenger
import org.jetbrains.projector.common.misc.Do
import org.jetbrains.projector.common.protocol.toClient.*

class ServerEventsProcessor {

  @OptIn(ExperimentalStdlibApi::class)
  fun process(
    commands: ToClientMessageType, pingStatistics: PingStatistics/*, typing: Typing, markdownPanelManager: MarkdownPanelManager,
              inputController: InputController*/
  ) {
    val drawCommandsEvents = mutableListOf<ServerDrawCommandsEvent>()

    commands.forEach { command ->
      Do exhaustive when (command) {
        is ServerWindowSetChangedEvent -> {
//          windowDataEventsProcessor.process(command)  // todo
//          markdownPanelManager.updatePlacements()  // todo
        }

        is ServerDrawCommandsEvent -> drawCommandsEvents.add(command)

        is ServerImageDataReplyEvent -> {
        }//windowDataEventsProcessor.windowManager.imageCacher.putImageData(
//          command.imageId,  // todo
//          command.imageData,
//        )

        is ServerCaretInfoChangedEvent -> {
//          typing.changeCaretInfo(command.data)  // todo
//          inputController.handleCaretInfoChange(command.data)  // todo
        }

        is ServerClipboardEvent -> handleServerClipboardChange(command)

        is ServerPingReplyEvent -> pingStatistics.onPingReply(command)

        is ServerMarkdownEvent -> {
        }//when (command) {  // todo
//          is ServerMarkdownEvent.ServerMarkdownShowEvent -> markdownPanelManager.show(command.panelId, command.show)
//          is ServerMarkdownEvent.ServerMarkdownResizeEvent -> markdownPanelManager.resize(command.panelId, command.size)
//          is ServerMarkdownEvent.ServerMarkdownMoveEvent -> markdownPanelManager.move(command.panelId, command.point)
//          is ServerMarkdownEvent.ServerMarkdownDisposeEvent -> markdownPanelManager.dispose(command.panelId)
//          is ServerMarkdownEvent.ServerMarkdownPlaceToWindowEvent -> markdownPanelManager.placeToWindow(command.panelId, command.windowId)
//          is ServerMarkdownEvent.ServerMarkdownSetHtmlEvent -> markdownPanelManager.setHtml(command.panelId, command.html)
//          is ServerMarkdownEvent.ServerMarkdownSetCssEvent -> markdownPanelManager.setCss(command.panelId, command.css)
//          is ServerMarkdownEvent.ServerMarkdownScrollEvent -> markdownPanelManager.scroll(command.panelId, command.scrollOffset)
//          is ServerMarkdownEvent.ServerMarkdownBrowseUriEvent -> browseUri(command.link)
//        }

        is ServerWindowColorsEvent -> {
          ProjectorUI.setColors(command.colors)
          // todo: should WindowManager.lookAndFeelChanged() be called here?
          OnScreenMessenger.lookAndFeelChanged()
        }
      }
    }

    // todo: determine the moment better
//    if (drawCommandsEvents.any { it.drawEvents.any { drawEvent -> drawEvent is ServerDrawStringEvent } }) {
//      typing.removeSpeculativeImage()
//    }  // todo

    drawCommandsEvents.sortWith(drawingOrderComparator)

    // todo
//    drawCommandsEvents.forEach { event ->
//      Do exhaustive when (val target = event.target) {
//        is ServerDrawCommandsEvent.Target.Onscreen -> windowDataEventsProcessor.draw(target.windowId, event.drawEvents)
//
//        is ServerDrawCommandsEvent.Target.Offscreen -> {
//          val offscreenProcessor = windowDataEventsProcessor.windowManager.imageCacher.getOffscreenProcessor(target)
//
//          val drawEvents = event.drawEvents.shrinkByPaintEvents()
//
//          val firstUnsuccessful = offscreenProcessor.processNew(drawEvents)
//          if (firstUnsuccessful != null) {
//            // todo: remember unsuccessful events and redraw pending ones as for windows
//            logger.error { "Encountered unsuccessful drawing for an offscreen surface ${target.pVolatileImageId}, skipping" }
//          }
//
//          windowDataEventsProcessor.drawPendingEvents()
//        }
//      }
//    }
  }

  fun onResized() {
//    windowDataEventsProcessor.onResized()  // todo
  }

  private fun handleServerClipboardChange(event: ServerClipboardEvent) {
    // todo
//    window.navigator.clipboard.writeText(event.stringContent)
//      .catch { logger.error { "Error writing clipboard: $it" } }
  }

// todo
//  private fun browseUri(link: String) {
//    val url = URL(link)
//
//    if(url.hostname == "localhost" || url.hostname == "127.0.0.1" || url.host == "::1") {
//      url.hostname = window.location.hostname
//      url.protocol = window.location.protocol
//    }
//
//    val popUpWindow = window.open(url.href, "_blank")
//
//    if (popUpWindow != null) {
//      popUpWindow.focus()  // browser has allowed it to be opened
//    }
//    else {
//      window.alert("To open $link, please allow popups for this website")  // browser has blocked it
//    }
//  }

  companion object {

    private val logger = Logger<ServerEventsProcessor>()

    // todo: sorting is added only as a hacky temporary workaround for PRJ-20.
    //       Please see commit description for details how this should be fixed
    private val drawingOrderComparator = compareBy<ServerDrawCommandsEvent>(
      // render offscreen surfaces first
      {
        when (it.target) {
          is ServerDrawCommandsEvent.Target.Offscreen -> 0
          is ServerDrawCommandsEvent.Target.Onscreen -> 1
        }
      },
      // render older surfaces last
      {
        when (val target = it.target) {
          is ServerDrawCommandsEvent.Target.Offscreen -> -target.pVolatileImageId
          is ServerDrawCommandsEvent.Target.Onscreen -> -target.windowId
        }
      },
    )
  }
}
