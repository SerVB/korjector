package org.jetbrains.projector.client.korge.state

import com.soywiz.korge.view.Stage
import org.jetbrains.projector.client.korge.WindowSizeController
import org.jetbrains.projector.common.protocol.toServer.ClientEvent

sealed class ClientAction {

  class Start(
    val stage: Stage,
    val stateMachine: ClientStateMachine,
    val url: String,
    val windowSizeController: WindowSizeController,
  ) : ClientAction()

  sealed class WebSocket : ClientAction() {

    class Open(val openingTimeStamp: Int) : WebSocket()
    class Message(val message: ByteArray) : WebSocket()
    class Close(val wasClean: Boolean, val code: Short, val reason: String) : WebSocket()
    class NoReplies(val elapsedTimeMs: Int) : WebSocket()
  }

  class AddEvent(val event: ClientEvent) : ClientAction()

  object Flush : ClientAction()

  object LoadAllFonts : ClientAction()

  object WindowResize : ClientAction()
}
