package org.jetbrains.projector.client.korge.window

interface DragEventsInterceptor {
  fun onMouseMove(x: Int, y: Int)
  fun onMouseDown(x: Int, y: Int): DragEventsInterceptor?
  fun onMouseUp(x: Int, y: Int)
  fun onMouseClick(x: Int, y: Int): DragEventsInterceptor? = null
}
