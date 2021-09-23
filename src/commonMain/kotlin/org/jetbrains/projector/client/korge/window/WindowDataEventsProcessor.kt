package org.jetbrains.projector.client.korge.window

import com.soywiz.klogger.Logger
import mainStage
import org.jetbrains.projector.client.common.SingleRenderingSurfaceProcessor.Companion.shrinkByPaintEvents
import org.jetbrains.projector.client.common.misc.ParamsProvider
import org.jetbrains.projector.common.protocol.toClient.ServerWindowEvent
import org.jetbrains.projector.common.protocol.toClient.ServerWindowSetChangedEvent
import org.jetbrains.projector.common.protocol.toClient.WindowData
import org.jetbrains.projector.common.protocol.toClient.WindowType

class WindowDataEventsProcessor(internal val windowManager: WindowManager) {

  private var excludedWindowIds = emptyList<Int>()

  fun drawPendingEvents() {
    windowManager.forEach(Window::drawPendingEvents)
  }

  fun onClose() {
    process(ServerWindowSetChangedEvent(emptyList()))
  }

  fun process(windowDataEvents: ServerWindowSetChangedEvent) {
    val excludedWindows = when (val selectedId = ParamsProvider.IDE_WINDOW_ID) {
      null -> emptyList()

      else -> windowDataEvents.windowDataList
        .filter { it.windowType == WindowType.IDEA_WINDOW }
        .sortedBy(WindowData::id)
        .filterIndexed { index, _ -> index != selectedId }
    }
    excludedWindowIds = excludedWindows.map(WindowData::id)
    val presentedWindows = windowDataEvents.windowDataList.subtract(excludedWindows)

    removeAbsentWindows(presentedWindows)

    presentedWindows.forEach { event ->
      val window = windowManager.getOrCreate(event)

      event.cursorType?.let { window.cursorType = it }
      window.title = event.title
      window.isShowing = event.isShowing
      window.bounds = event.bounds
      window.zIndex = (event.zOrder - presentedWindows.size) * WindowManager.zIndexStride
    }

    setTitle(presentedWindows)
//    setFavIcon(presentedWindows)  // todo
  }

  private fun setTitle(presentedWindows: Iterable<WindowData>) {
    val topmostWindowTitle = presentedWindows
      .filter(WindowData::isShowing)
      .sortedByDescending(WindowData::zOrder)
      .firstNotNullOfOrNull(WindowData::title)

    mainStage.gameWindow.title = topmostWindowTitle ?: DEFAULT_TITLE
  }

//  private fun setFavIcon(presentedWindows: Iterable<WindowData>) {  // todo
//    val topmostWindowIconIds = presentedWindows
//      .filter(WindowData::isShowing)
//      .sortedByDescending(WindowData::zOrder)
//      .mapNotNull(WindowData::icons)
//      .firstOrNull(List<*>::isNotEmpty)
//
//    fun selectIcon(icons: List<ImageId>?) = icons?.firstOrNull()  // todo
//
//    val selectedIconId = selectIcon(topmostWindowIconIds)
//
//    val selectedIconUrl = when (val selectedIcon = selectedIconId?.let { windowManager.imageCacher.getImageData(it) }) {
//      is HTMLCanvasElement -> selectedIcon.toDataURL()
//      is HTMLImageElement -> selectedIcon.src
//      else -> "pj.svg"
//    }
//
//    fun getFavIconLink() = document.querySelector("link[rel*='icon']") ?: document.createElement("link")
//
//    val link = (getFavIconLink() as HTMLLinkElement).apply {
//      type = "image/x-icon"
//      rel = "shortcut icon"
//      href = selectedIconUrl
//    }
//    document.head!!.appendChild(link)
//  }

  fun draw(windowId: Int, commands: List<ServerWindowEvent>) {
    if (windowId in excludedWindowIds) {
      return
    }

    val window = windowManager[windowId]

    if (window == null) {
      logger.error { "Skipping nonexistent window: $windowId" }
      return
    }

    val newEvents = commands.shrinkByPaintEvents()

    if (newEvents.isNotEmpty()) {
      window.newDrawEvents.addAll(newEvents)
      window.drawNewEvents()
    }
  }

  private fun removeAbsentWindows(presentedWindows: Iterable<WindowData>) {
    val presentedWindowIds = presentedWindows.map(WindowData::id).toSet()

    windowManager.cleanup(presentedWindowIds)
  }

  fun onResized() {
    windowManager.forEach(Window::applyBounds)
  }

  companion object {
    private val logger = Logger<WindowDataEventsProcessor>()
    private const val DEFAULT_TITLE = "korjector"
  }
}
