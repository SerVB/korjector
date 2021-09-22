package org.jetbrains.projector.client.common.canvas

import com.soywiz.korim.font.Font
import org.jetbrains.projector.client.common.canvas.Canvas.ImageSource
import org.jetbrains.projector.client.common.canvas.PaintColor.Gradient
import org.jetbrains.projector.common.protocol.data.PathSegment
import org.jetbrains.projector.common.protocol.data.Point

interface Context2d {
  fun clearRect(x: Double, y: Double, w: Double, h: Double)
  fun drawImage(imageSource: ImageSource, x: Double, y: Double)
  fun drawImage(imageSource: ImageSource, x: Double, y: Double, dw: Double, dh: Double)
  fun drawImage(
    imageSource: ImageSource,
    sx: Double,
    sy: Double,
    sw: Double,
    sh: Double,
    dx: Double,
    dy: Double,
    dw: Double,
    dh: Double,
  )

  fun beginPath()
  fun closePath()
  fun stroke()
  fun fill(fillRule: FillRule = FillRule.NONZERO)
  fun fillRect(x: Double, y: Double, w: Double, h: Double)
  fun moveTo(x: Double, y: Double)
  fun moveBySegments(segments: List<PathSegment>)
  fun moveByPoints(points: List<Point>)
  fun lineTo(x: Double, y: Double)
  fun roundedRect(x: Double, y: Double, w: Double, h: Double, r1: Double, r2: Double)
  fun rect(x: Double, y: Double, w: Double, h: Double)
  fun ellipse(
    x: Double,
    y: Double,
    radiusX: Double,
    radiusY: Double,
    rotation: Double,
    startAngle: Double,
    endAngle: Double,
    anticlockwise: Boolean = false,
  )

  fun save()
  fun restore()
  fun setFillStyle(color: PaintColor?)
  fun setStrokeStyle(color: PaintColor?)
  fun setGlobalAlpha(alpha: Double)
  fun setGlobalCompositeOperation(type: CompositeOperationType)
  fun setFont(f: Font, size: Double)
  fun setLineWidth(lineWidth: Double)
  fun strokeRect(x: Double, y: Double, w: Double, h: Double)
  fun strokeText(text: String, x: Double, y: Double)
  fun fillText(text: String, x: Double, y: Double)
  fun scale(x: Double, y: Double)
  fun rotate(angle: Double)
  fun translate(x: Double, y: Double)
  fun transform(m11: Double, m12: Double, m21: Double, m22: Double, dx: Double, dy: Double)
  fun bezierCurveTo(cp1x: Double, cp1y: Double, cp2x: Double, cp2y: Double, x: Double, y: Double)
  fun quadraticCurveTo(cpx: Double, cpy: Double, x: Double, y: Double)
  fun setLineJoin(lineJoin: LineJoin)
  fun setMiterLimit(limit: Double)
  fun setLineCap(lineCap: LineCap)
  fun setTextBaseline(baseline: TextBaseline)
  fun setTextAlign(align: TextAlign)
  fun setTransform(m11: Double, m12: Double, m21: Double, m22: Double, dx: Double, dy: Double)
  fun setLineDash(lineDash: DoubleArray)
  fun setLineDashOffset(offset: Double)
  fun measureText(str: String): Point

  fun clip(fillRule: FillRule? = null)

  fun createLinearGradient(x0: Double, y0: Double, x1: Double, y1: Double): Gradient

  fun getTransform(): Matrix

  enum class LineJoin {
    BEVEL, MITER, ROUND
  }

  enum class LineCap {
    BUTT, ROUND, SQUARE
  }

  enum class TextBaseline {
    ALPHABETIC, BOTTOM, HANGING, IDEOGRAPHIC, MIDDLE, TOP
  }

  enum class TextAlign {
    CENTER, END, LEFT, RIGHT, START
  }

  enum class FillRule {
    NONZERO, EVENODD
  }

  enum class CompositeOperationType {
    SRC_OVER,
    DST_OVER,
    SRC_IN,
    DST_IN,
    SRC_OUT,
    DST_OUT,
    SRC_ATOP,
    DST_ATOP,
    XOR,
  }

  /**
   * Transformation matrix 2d
   *     a c e
   *   [ b d f ]
   *     0 0 1
   *
   *   a Horizontal scaling. A value of 1 results in no scaling.
   *   b Vertical skewing.
   *   c Horizontal skewing.
   *   d Vertical scaling. A value of 1 results in no scaling.
   *   e Horizontal translation (moving).
   *   f Vertical translation (moving).
   */
  class Matrix(val a: Double, val b: Double, val c: Double, val d: Double, val e: Double, val f: Double) {
    constructor(list: List<Double>) : this(list[0], list[1], list[2], list[3], list[4], list[5])

    companion object {
      private val IDENTITY = Matrix(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)

      val IDENTITY_LIST = with(IDENTITY) { listOf(a, b, c, d, e, f) }
    }
  }
}
