package org.jetbrains.projector.client.korge.window

import com.soywiz.klogger.Logger
import org.jetbrains.projector.client.korge.state.LafListener

object OnScreenMessenger : LafListener {

  private val logger = Logger<OnScreenMessenger>()

  // todo: port to korge
//  private val header = WindowHeader().apply {
//    undecorated = true
//    visible = false
//  }
//
//  private val div = (document.createElement("div") as HTMLDivElement).apply {
//    style.apply {
//      position = "fixed"
//      zIndex = "567"
//
//      // put to center:
//      width = "400px"
//      top = "50%"
//      left = "50%"
//      transform = "translate(-50%, -50%)"
//
//      padding = "5px"
//    }
//  }
//
//  private val text = (document.createElement("div") as HTMLDivElement).apply {
//    div.appendChild(this)
//  }
//
//  private val reload = (document.createElement("div") as HTMLDivElement).apply {
//    innerHTML = "<p>If you wish, you can try to <a onclick='location.reload();' href=''>reconnect</a>.</p>"
//
//    div.appendChild(this)
//  }

  init {
    lookAndFeelChanged()
  }

  fun showText(title: String, content: String, canReload: Boolean) {
    logger.info { "$title - $content" }

    // todo: port to korge
//    header.title = title
//    text.innerHTML = "<p>$content</p>"
//
//    reload.style.display = canReload.toDisplayType()
//
//    if (div.parentElement == null) {
//      document.body!!.appendChild(div)
//    }
//
//    val userScalingRatio = ParamsProvider.USER_SCALING_RATIO
//    val mainDivBounds = div.getBoundingClientRect()
//    header.bounds = CommonRectangle(
//      mainDivBounds.x * userScalingRatio,
//      (mainDivBounds.y - ProjectorUI.headerHeight) * userScalingRatio,
//      div.clientWidth * userScalingRatio,
//      ProjectorUI.headerHeight * userScalingRatio
//    )
//    header.visible = true
//    header.zIndex = div.style.zIndex.toInt()
//    header.draw()
  }

  fun hide() {
    // todo: port to korge
//    if (div.parentElement != null) {
//      div.remove()
//    }
//    header.visible = false
  }

  override fun lookAndFeelChanged() {
    // todo: port to korge
//    header.lookAndFeelChanged()
//
//    div.style.apply {
//      backgroundColor = ProjectorUI.windowHeaderInactiveBackgroundArgb.argbIntToRgbaString()
//      borderLeft = ProjectorUI.borderStyle
//      borderRight = ProjectorUI.borderStyle
//      borderBottom = ProjectorUI.borderStyle
//      borderRadius = "0 0 ${ProjectorUI.borderRadius}px ${ProjectorUI.borderRadius}px"
//      color = ProjectorUI.windowHeaderActiveTextArgb.argbIntToRgbaString()
//    }
  }
}
