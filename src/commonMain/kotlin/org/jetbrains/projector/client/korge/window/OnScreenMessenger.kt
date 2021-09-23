package org.jetbrains.projector.client.korge.window

import com.soywiz.klogger.Logger
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.solidRect
import com.soywiz.korge.view.text
import com.soywiz.korge.view.xy
import mainStage
import org.jetbrains.projector.client.common.canvas.Extensions.argbIntToRgbaString
import org.jetbrains.projector.client.common.misc.ParamsProvider
import org.jetbrains.projector.client.korge.state.LafListener
import org.jetbrains.projector.client.korge.state.ProjectorUI
import org.jetbrains.projector.common.protocol.data.CommonRectangle

object OnScreenMessenger : LafListener {

  private val logger = Logger<OnScreenMessenger>()

  private val header by lazy {
    WindowHeader(mainStage).apply {
      undecorated = true
      visible = false
      lookAndFeelChanged()
    }
  }

  private val div by lazy {
    Container()
  }

  private val divBg by lazy {
    div.solidRect(400, 300, ProjectorUI.windowHeaderInactiveBackgroundArgb.argbIntToRgbaString())
  }

  private val text by lazy {
    div.text("", color = ProjectorUI.windowHeaderActiveTextArgb.argbIntToRgbaString())
      .xy(5, 5)
  }

//  private val reload = (document.createElement("div") as HTMLDivElement).apply {
//    innerHTML = "<p>If you wish, you can try to <a onclick='location.reload();' href=''>reconnect</a>.</p>"
//
//    div.appendChild(this)
//  }

  fun showText(title: String, content: String, canReload: Boolean) {
    logger.info { "$title - $content" }

    divBg // cause initialization

    header.title = title
    text.text = content

//    reload.style.display = canReload.toDisplayType()  // todo: port to korge

    if (div.parent == null) {
      mainStage.addChild(div)
    }

    val width = 400
    val x = mainStage.unscaledWidth / 2 - width / 2
    val y = 100

    val userScalingRatio = ParamsProvider.USER_SCALING_RATIO
    header.bounds = CommonRectangle(
      x * userScalingRatio,
      (y - ProjectorUI.headerHeight) * userScalingRatio,
      width * userScalingRatio + 1,  // todo: for some reason header is about 1 pixel less then needed, so adding 1
      ProjectorUI.headerHeight * userScalingRatio
    )
    header.visible = true
//    header.zIndex = div.style.zIndex.toInt()  // todo
    header.draw()

    div.xy(x * userScalingRatio, y * userScalingRatio)
  }

  fun hide() {
    if (div.parent != null) {
      div.removeFromParent()
    }
    header.visible = false
  }

  override fun lookAndFeelChanged() {
    header.lookAndFeelChanged()
    updateDivStyle()
  }

  private fun updateDivStyle() {
    text.color = ProjectorUI.windowHeaderActiveTextArgb.argbIntToRgbaString()
    divBg.color = ProjectorUI.windowHeaderInactiveBackgroundArgb.argbIntToRgbaString()

    // todo: port to korge
//    div.style.apply {
//      borderLeft = ProjectorUI.borderStyle
//      borderRight = ProjectorUI.borderStyle
//      borderBottom = ProjectorUI.borderStyle
//      borderRadius = "0 0 ${ProjectorUI.borderRadius}px ${ProjectorUI.borderRadius}px"
//    }
  }
}
