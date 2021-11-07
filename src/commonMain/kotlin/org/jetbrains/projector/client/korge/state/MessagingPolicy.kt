package org.jetbrains.projector.client.korge.state

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mainStage

sealed class MessagingPolicy(
  protected val isFlushNeeded: () -> Boolean,
  protected val flush: () -> Unit,
) {

  abstract fun onHandshakeFinished()

  abstract fun onToClientMessage()

  abstract fun onAddEvent()

  protected fun flushIfNeeded() {
    if (isFlushNeeded()) {
      flush()
    }
  }

  class Unbuffered(isFlushNeeded: () -> Boolean, flush: () -> Unit) : MessagingPolicy(isFlushNeeded, flush) {

    override fun onHandshakeFinished() {
      flush()
    }

    override fun onToClientMessage() {
      flushIfNeeded()
    }

    override fun onAddEvent() {
      flush()
    }
  }

  class Buffered(private val timeout: Int, isFlushNeeded: () -> Boolean, flush: () -> Unit) :
    MessagingPolicy(isFlushNeeded, flush) {

    override fun onHandshakeFinished() {
      flush()
    }

    private fun scheduleFlushIfNeeded() {
      mainStage.launch {
        delay(timeout.toLong())
        flushIfNeeded()
      }
    }

    override fun onToClientMessage() {
      scheduleFlushIfNeeded()
    }

    override fun onAddEvent() {
      scheduleFlushIfNeeded()
    }
  }
}
