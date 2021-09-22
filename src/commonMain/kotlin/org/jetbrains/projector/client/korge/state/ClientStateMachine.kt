package org.jetbrains.projector.client.korge.state

import com.soywiz.klogger.Logger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class ClientStateMachine {

  private val scope = GlobalScope

  private val eventQueue = Channel<ClientAction>(capacity = Channel.UNLIMITED)

  private var currentState: ClientState = ClientState.UninitializedPage

  fun fire(action: ClientAction) {
    scope.launch {
      eventQueue.send(action)
    }
  }

  fun runMainLoop() {
    scope.launch {
      while (true) {
        val action = eventQueue.receive()
        try {
          currentState = currentState.consume(action)
        } catch (t: Throwable) {
          logger.error { t.stackTraceToString() }
          logger.error { "Error consuming action $action, skipping the action" }
        }
      }
    }
  }

  private companion object {

    private val logger = Logger<ClientStateMachine>()
  }
}
