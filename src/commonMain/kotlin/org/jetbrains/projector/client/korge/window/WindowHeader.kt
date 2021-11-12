package org.jetbrains.projector.client.korge.window

import com.soywiz.korge.input.onMove
import com.soywiz.korge.input.onOut
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.image
import com.soywiz.korge.view.xy
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.font.DefaultTtfFont
import com.soywiz.korim.format.SVG
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.IPoint
import org.jetbrains.projector.client.common.canvas.*
import org.jetbrains.projector.client.common.canvas.buffering.createRenderingSurface
import org.jetbrains.projector.client.common.misc.ParamsProvider
import org.jetbrains.projector.client.korge.state.LafListener
import org.jetbrains.projector.client.korge.state.ProjectorUI
import org.jetbrains.projector.common.protocol.data.CommonRectangle
import org.jetbrains.projector.common.protocol.data.Point
import kotlin.math.roundToInt
import kotlin.native.concurrent.*

class WindowHeader(parent: Container, var title: String? = null) : DragEventsInterceptor, LafListener {

  @ThreadLocal
  companion object {
    lateinit var closeIconNormal: Bitmap
    lateinit var closeIconHover: Bitmap

    suspend fun initIcons() {
      closeIconNormal = resourcesVfs["close.svg"].readBitmap(SVG)
      closeIconHover = resourcesVfs["closeHover.svg"].readBitmap(SVG)
    }
  }

  private var closeIcon = closeIconNormal

  private val canvas = parent.image(NativeImageOrBitmap32(42, 42, USE_NATIVE_IMAGE))
  private val headerRenderingSurface = createRenderingSurface(canvas)

  private var dragStarted = false
  private var lastPoint = Point(-1.0, -1.0)

  var onMove: (deltaX: Int, deltaY: Int) -> Unit = { _, _ -> }
  var onClose: () -> Unit = {}

  /** Undecorated windows have no any buttons on header. */
  var undecorated: Boolean = false

  private var clientCloseBounds: CommonRectangle = CommonRectangle(0.0, 0.0, 0.0, 0.0)
  var bounds: CommonRectangle = CommonRectangle(0.0, 0.0, 0.0, 0.0)
    set(value) {
      val scalingRatio = ParamsProvider.SCALING_RATIO / ParamsProvider.USER_SCALING_RATIO
      headerRenderingSurface.scalingRatio = ParamsProvider.SCALING_RATIO

      headerRenderingSurface.setBounds(
        width = (value.width * scalingRatio).roundToInt(),
        height = (value.height * scalingRatio).roundToInt()
      )

      if (field == value) {
        return
      }

      field = value

      clientCloseBounds = CommonRectangle(value.x + value.width - value.height, value.y, value.height, value.height)

      canvas.xy(value.x, value.y)
    }

  var zIndex: Int = 0
    set(value) {
      if (field != value) {
        field = value
//        style.zIndex = "$zIndex"  // todo
      }
    }

  var visible: Boolean = true
    set(value) {
      if (field != value) {
        field = value
        canvas.visible = value
      }
    }

  init {
    lookAndFeelChanged()

    canvas.onMove { onMouseMove(it.input.mouse) }
    canvas.onOut { onMouseLeave() }
  }

  fun dispose() {
    canvas.removeFromParent()
  }

  override fun lookAndFeelChanged() {
    // todo
//    style.borderTop = ProjectorUI.borderStyle
//    style.borderLeft = ProjectorUI.borderStyle
//    style.borderRight = ProjectorUI.borderStyle
  }

  private fun onMouseLeave() {
    if (closeIcon != closeIconNormal) {
      closeIcon = closeIconNormal
      draw()
    }
  }

  private fun onMouseMove(pos: IPoint) {
    val newCloseIcon = if (clientCloseBounds.contains(pos.x.toInt(), pos.y.toInt())) closeIconHover else closeIconNormal
    if (newCloseIcon != closeIcon) {
      closeIcon = newCloseIcon
      draw()
    }
  }

  override fun onMouseMove(x: Int, y: Int) {
    if (dragStarted) {
      onMove(
        ((x - lastPoint.x) / ParamsProvider.USER_SCALING_RATIO).toInt(),
        ((y - lastPoint.y) / ParamsProvider.USER_SCALING_RATIO).toInt()
      )
      lastPoint = Point(x.toDouble(), y.toDouble())
    }
  }

  override fun onMouseDown(x: Int, y: Int): DragEventsInterceptor? {
    if (!bounds.contains(x, y)) {
      return null
    }

    dragStarted = true
    lastPoint = Point(x.toDouble(), y.toDouble())
    return this
  }

  override fun onMouseUp(x: Int, y: Int) {
    dragStarted = false
  }

  override fun onMouseClick(x: Int, y: Int): DragEventsInterceptor? {
    if (!undecorated && clientCloseBounds.contains(x, y)) {
      onClose()
      return this
    }
    return null
  }

  fun draw() {
    val context = headerRenderingSurface.canvas.context2d
    val offset = ProjectorUI.crossOffset * headerRenderingSurface.scalingRatio

    // Fill header background.
    context.setFillStyle(PaintColor.SolidColor(ProjectorUI.windowHeaderActiveBackgroundArgb))
    // fill like two rounded corners on the top but sharp corners on the bottom
    context.roundedRect(
      0.0,
      0.0,
      canvas.width,
      canvas.height,
      ProjectorUI.borderRadius.toDouble(),
      ProjectorUI.borderRadius.toDouble()
    )
    context.fill()
    context.fillRect(0.0, ProjectorUI.borderRadius.toDouble(), canvas.width, canvas.height)

    if (!undecorated) {
      // Cross on close button.
      context.drawImage(
        KorgeCanvas.DomImageSource(closeIcon),
        canvas.width - canvas.height + offset,
        offset,
        canvas.height - offset * 2,
        canvas.height - offset * 2
      )
    }

    if (title != null) {
      context.setFillStyle(PaintColor.SolidColor(ProjectorUI.windowHeaderActiveTextArgb))
      val fontSize = 0.5 * ProjectorUI.headerHeight * headerRenderingSurface.scalingRatio
      context.setFont(DefaultTtfFont, fontSize)
      val (textW, textH) = context.measureText(title!!)
      val textX = canvas.width / 2 - textW / 2
      val textY = canvas.height / 2 - textH / 2
      context.fillText(title!!, textX, textY + fontSize)
    }

    headerRenderingSurface.flush()
  }
}
