package org.jetbrains.projector.client.common.canvas

import com.soywiz.korge.view.Image
import com.soywiz.korim.color.Colors
import com.soywiz.korim.font.DefaultTtfFont
import com.soywiz.korim.font.Font
import com.soywiz.korim.paint.GradientPaint
import com.soywiz.korim.vector.CompositeMode
import com.soywiz.korim.vector.CompositeOperation
import com.soywiz.korma.geom.radians
import com.soywiz.korma.geom.vector.arc
import com.soywiz.korma.geom.vector.arcTo
import com.soywiz.korma.geom.vector.ellipse
import com.soywiz.korma.geom.vector.rect
import org.jetbrains.projector.client.common.canvas.Canvas.ImageSource
import org.jetbrains.projector.client.common.canvas.Context2d.*
import org.jetbrains.projector.client.common.canvas.Extensions.argbIntToRgbaString
import org.jetbrains.projector.client.common.canvas.PaintColor.Gradient
import org.jetbrains.projector.client.common.canvas.PaintColor.SolidColor
import org.jetbrains.projector.common.misc.Do
import org.jetbrains.projector.common.protocol.data.PathSegment
import org.jetbrains.projector.common.protocol.data.Point
import com.soywiz.korim.vector.Context2d as KorgeContext2d
import com.soywiz.korma.geom.vector.LineCap as KorgeLineCap
import com.soywiz.korma.geom.vector.LineJoin as KorgeLineJoin

internal class KorgeContext2d(private val myCanvas: Image) : Context2d {

  private val lastSize = myCanvas.bitmap.bmp.size
  private var lastContext2d = myCanvas.bitmap.bmp.getContext2d()
  private val myContext2d: KorgeContext2d
    get() {
      val currentSize = myCanvas.bitmap.bmp.size
      if (currentSize != lastSize) {
        lastSize.setTo(currentSize)
        lastContext2d = myCanvas.bitmap.bmp.getContext2d()
      }
      return lastContext2d
    }

  private fun convertLineJoin(lineJoin: LineJoin): KorgeLineJoin {
    return when (lineJoin) {
      LineJoin.BEVEL -> KorgeLineJoin.BEVEL
      LineJoin.MITER -> KorgeLineJoin.MITER
      LineJoin.ROUND -> KorgeLineJoin.ROUND
    }
  }

  private fun convertLineCap(lineCap: LineCap): KorgeLineCap {
    return when (lineCap) {
      LineCap.BUTT -> KorgeLineCap.BUTT
      LineCap.ROUND -> KorgeLineCap.ROUND
      LineCap.SQUARE -> KorgeLineCap.SQUARE
    }
  }

  private fun convertCompositeOperationType(type: CompositeOperationType): CompositeOperation {
    return when (type) {
      CompositeOperationType.SRC_OVER -> CompositeMode.SOURCE_OVER
      CompositeOperationType.DST_OVER -> CompositeMode.DESTINATION_OVER
      CompositeOperationType.SRC_IN -> CompositeMode.SOURCE_IN
      CompositeOperationType.DST_IN -> CompositeMode.DESTINATION_IN
      CompositeOperationType.SRC_OUT -> CompositeMode.SOURCE_OUT
      CompositeOperationType.DST_OUT -> CompositeMode.DESTINATION_OUT
      CompositeOperationType.SRC_ATOP -> CompositeMode.SOURCE_ATOP
      CompositeOperationType.DST_ATOP -> CompositeMode.DESTINATION_ATOP
      CompositeOperationType.XOR -> CompositeMode.XOR
    }
  }

  override fun drawImage(imageSource: ImageSource, x: Double, y: Double) {
//    myContext2d.drawImage(imageSource.asPlatformImageSource().canvasElement, x, y)  // todo
  }

  override fun drawImage(imageSource: ImageSource, x: Double, y: Double, dw: Double, dh: Double) {
//    myContext2d.drawImage(imageSource.asPlatformImageSource().canvasElement, x, y, dw, dh)  // todo
  }

  override fun drawImage(
    imageSource: ImageSource,
    sx: Double,
    sy: Double,
    sw: Double,
    sh: Double,
    dx: Double,
    dy: Double,
    dw: Double,
    dh: Double,
  ) {
//    myContext2d.drawImage(imageSource.asPlatformImageSource().canvasElement, sx, sy, sw, sh, dx, dy, dw, dh)  // todo
  }

  override fun beginPath() {
    myContext2d.beginPath()
  }

  override fun closePath() {
    myContext2d.close()
  }

  override fun stroke() {
    myContext2d.stroke()
  }

  override fun fill(fillRule: FillRule) {
    // todo: use fillRule
    myContext2d.fill()
  }

  override fun fillRect(x: Double, y: Double, w: Double, h: Double) {
    myContext2d.fillRect(x, y, w, h)
  }

  override fun moveTo(x: Double, y: Double) {
    myContext2d.moveTo(x, y)
  }

  override fun moveBySegments(segments: List<PathSegment>) {
    myContext2d.moveBySegments(segments)
  }

  override fun moveByPoints(points: List<Point>) {
    myContext2d.moveByPoints(points)
  }

  override fun lineTo(x: Double, y: Double) {
    myContext2d.lineTo(x, y)
  }

  override fun roundedRect(x: Double, y: Double, w: Double, h: Double, r1: Double, r2: Double) {
    moveTo(x + r1, y)
    arcTo(x + w, y, x + w, y + h, r1)
    arcTo(x + w, y + h, x, y + h, r2)
    arcTo(x, y + h, x, y, r1)
    arcTo(x, y, x + w, y, r2)
  }

  fun arcTo(x1: Double, y1: Double, x2: Double, y2: Double, radius: Double) {
    myContext2d.arcTo(x1, y1, x2, y2, radius)
  }

  fun arc(x: Double, y: Double, radius: Double, startAngle: Double, endAngle: Double) {
    myContext2d.arc(x, y, radius, startAngle.radians, endAngle.radians)
  }

  override fun rect(x: Double, y: Double, w: Double, h: Double) {
    myContext2d.rect(x, y, w, h)
  }

  override fun ellipse(
    x: Double,
    y: Double,
    radiusX: Double,
    radiusY: Double,
    rotation: Double,
    startAngle: Double,
    endAngle: Double,
    anticlockwise: Boolean,
  ) {
    myContext2d.ellipse(x, y, radiusX, radiusY)  // todo: use other parameters
  }

  override fun save() {
    myContext2d.save()
  }

  override fun restore() {
    try {
      myContext2d.restore()
    } catch (e: IndexOutOfBoundsException) {
      // nothing to restore
    }
  }

  override fun setFillStyle(color: PaintColor?) {
    myContext2d.fillStyle = color?.extract() ?: Colors.MAGENTA
  }

  override fun setStrokeStyle(color: PaintColor?) {
    myContext2d.strokeStyle = color?.extract() ?: Colors.MAGENTA
  }

  override fun setGlobalAlpha(alpha: Double) {
    myContext2d.globalAlpha = alpha
  }

  override fun setGlobalCompositeOperation(type: CompositeOperationType) {
    myContext2d.globalCompositeOperation = convertCompositeOperationType(type)
  }

  override fun setFont(f: Font, size: Double) {
    myContext2d.font = DefaultTtfFont
    myContext2d.fontSize = size
  }

  override fun setLineWidth(lineWidth: Double) {
    myContext2d.lineWidth = lineWidth
  }

  override fun strokeRect(x: Double, y: Double, w: Double, h: Double) {
    myContext2d.strokeRect(x, y, w, h)
  }

  override fun strokeText(text: String, x: Double, y: Double) {
    myContext2d.strokeText(text, x, y)
  }

  override fun fillText(text: String, x: Double, y: Double) {
    myContext2d.fillText(text, x, y)
  }

  override fun scale(x: Double, y: Double) {
    myContext2d.scale(x, y)
  }

  override fun rotate(angle: Double) {
    myContext2d.rotate(angle)
  }

  override fun translate(x: Double, y: Double) {
    myContext2d.translate(x, y)
  }

  override fun transform(m11: Double, m12: Double, m21: Double, m22: Double, dx: Double, dy: Double) {
    myContext2d.transform(m11, m12, m21, m22, dx, dy)
  }

  override fun bezierCurveTo(cp1x: Double, cp1y: Double, cp2x: Double, cp2y: Double, x: Double, y: Double) {
    myContext2d.cubicTo(cp1x, cp1y, cp2x, cp2y, x, y)
  }

  override fun quadraticCurveTo(cpx: Double, cpy: Double, x: Double, y: Double) {
    myContext2d.quadTo(cpx, cpy, x, y)
  }

  override fun setLineJoin(lineJoin: LineJoin) {
    myContext2d.lineJoin = convertLineJoin(lineJoin)
  }

  override fun setMiterLimit(limit: Double) {
//    myContext2d.miter = limit  // todo
  }

  override fun setLineCap(lineCap: LineCap) {
    myContext2d.lineCap = convertLineCap(lineCap)
  }

  override fun setTextBaseline(baseline: TextBaseline) {
//    myContext2d.textBaseline = convertTextBaseline(baseline)  // todo
  }

  override fun setTextAlign(align: TextAlign) {
//    myContext2d.textAlign = convertTextAlign(align)  // todo
  }

  override fun setTransform(m11: Double, m12: Double, m21: Double, m22: Double, dx: Double, dy: Double) {
    myContext2d.setTransform(m11, m12, m21, m22, dx, dy)
  }

  override fun setLineDash(lineDash: DoubleArray) {
//    myContext2d.setLineDash(lineDash.toTypedArray())  // todo
  }

  override fun setLineDashOffset(offset: Double) {
//    myContext2d.lineDashOffset = offset  // todo
  }

  override fun measureText(str: String): Point {
    val measure = myContext2d.getTextBounds(str)
    return Point(measure.width, measure.allLineHeight)  // todo: recheck the second parameter
  }

  override fun clip(fillRule: FillRule?) {
//    fillRule?.let { myContext2d.clip(convertFillRule(it)) } ?:  // todo
    myContext2d.clip()
  }

  override fun createLinearGradient(x0: Double, y0: Double, x1: Double, y1: Double): Gradient {
    return DOMGradient(myContext2d.createLinearGradient(x0, y0, x1, y1))
  }

  override fun getTransform(): Matrix {
    return with(myContext2d.state.transform) {
      Matrix(a, b, c, d, tx, ty)
    }
  }

  override fun clearRect(x: Double, y: Double, w: Double, h: Double) {
//    myContext2d.clearRect(x, y, w, h)  // todo
  }

  private fun ImageSource.asPlatformImageSource(): KorgeCanvas.DomImageSource {
    return this as KorgeCanvas.DomImageSource
  }

  @Suppress("IMPLICIT_CAST_TO_ANY")
  fun PaintColor.extract() = when (this) {
    is SolidColor -> argb.argbIntToRgbaString()
    is Gradient -> (this as DOMGradient).canvasGradient
  }

  class DOMGradient(val canvasGradient: GradientPaint) : Gradient() {
    override fun addColorStop(offset: Double, argb: Int) {
      canvasGradient.addColorStop(offset, argb.argbIntToRgbaString())
    }
  }

  companion object {
    fun KorgeContext2d.moveBySegments(segments: List<PathSegment>) {
      segments.forEach {
        Do exhaustive when (it) {
          is PathSegment.MoveTo -> this.moveTo(it.point.x, it.point.y)
          is PathSegment.LineTo -> this.lineTo(it.point.x, it.point.y)
          is PathSegment.QuadTo -> this.quadTo(  // todo: check parameters ordering
            it.point1.x, it.point1.y,
            it.point2.x, it.point2.y
          )
          is PathSegment.CubicTo -> this.cubicTo(  // todo: check parameters ordering
            it.point1.x, it.point1.y,
            it.point2.x, it.point2.y,
            it.point3.x, it.point3.y
          )
          is PathSegment.Close -> this.close()
        }
      }
    }

    fun KorgeContext2d.moveByPoints(points: List<Point>) {
      points.forEachIndexed { i, point ->
        if (i == 0) {
          moveTo(point.x, point.y)
        } else {
          lineTo(point.x, point.y)
        }
      }
    }
  }
}
