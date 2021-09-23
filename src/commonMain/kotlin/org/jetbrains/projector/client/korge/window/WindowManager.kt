package org.jetbrains.projector.client.korge.window

import com.soywiz.korge.view.Image
import org.jetbrains.projector.client.common.misc.ImageCacher
import org.jetbrains.projector.client.korge.state.ClientStateMachine
import org.jetbrains.projector.client.korge.state.LafListener
import org.jetbrains.projector.common.protocol.toClient.WindowData

class WindowManager(private val stateMachine: ClientStateMachine, val imageCacher: ImageCacher) : Iterable<Window>,
  LafListener {

  companion object {
    const val zIndexStride = 10
  }

  private val windows = mutableMapOf<Int, Window>()

  fun getWindowCanvas(windowId: Int): Image? = windows[windowId]?.canvas

  fun getWindowZIndex(windowId: Int): Int? = windows[windowId]?.zIndex

  /** Returns topmost visible window, containing point. Contain check includes window header and borders.  */
  fun getTopWindow(x: Int, y: Int): Window? =
    windows.values.filter { it.isShowing && it.contains(x, y) }.maxByOrNull { it.zIndex }

  fun getOrCreate(windowData: WindowData): Window {
    return windows.getOrPut(windowData.id) { Window(windowData, stateMachine, imageCacher) }
  }

  fun cleanup(presentedWindowIds: Set<Int>) {
    windows.keys.retainAll { id ->
      if (id in presentedWindowIds) {
        true
      } else {
        windows.getValue(id).dispose()
        false
      }
    }
  }

  override fun lookAndFeelChanged() {
    windows.forEach { it.value.lookAndFeelChanged() }
  }

  operator fun get(windowId: Int): Window? = windows[windowId]

  override fun iterator(): Iterator<Window> = windows.values.iterator()

  fun bringToFront(window: Window) {
    val topWindow = windows.maxByOrNull { it.value.zIndex }?.value ?: return
    if (topWindow == window) {
      return
    }

    val currentZIndex = window.zIndex
    val topZIndex = topWindow.zIndex

    windows.filter { it.value.zIndex in currentZIndex..topZIndex }.forEach { it.value.zIndex -= zIndexStride }
    window.zIndex = topZIndex
  }
}
