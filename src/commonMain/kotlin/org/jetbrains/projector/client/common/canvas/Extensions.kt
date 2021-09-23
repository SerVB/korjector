package org.jetbrains.projector.client.common.canvas

import com.soywiz.klogger.Logger
import com.soywiz.korim.color.RGBA
import org.jetbrains.projector.common.misc.Do
import org.jetbrains.projector.common.protocol.data.AlphaCompositeRule
import org.jetbrains.projector.common.protocol.data.CommonPath
import org.jetbrains.projector.common.protocol.data.StrokeData
import kotlin.math.absoluteValue

object Extensions {

  private val logger = Logger<Extensions>()

  fun Canvas.resizeSavingImage(width: Int, height: Int) {
    if (this.width == width && this.height == height) {
      return  // prevents clearing canvas
    }

    if (this.width == 0 || this.height == 0) {
      this.width = width
      this.height = height

      return
    }

    val snapshot = this.takeSnapshot()
    val scalingChanged = (this.width.toDouble() / width - this.height.toDouble() / height).absoluteValue < 0.1

    this.width = width
    this.height = height

    if (scalingChanged) {
      this.context2d.drawImage(
        snapshot,
        0.0,
        0.0,
        this.width.toDouble(),
        this.height.toDouble()
      )  // save previous image with scaling
    } else {
      this.context2d.drawImage(snapshot, 0.0, 0.0)  // save previous image with the same size to avoid stretching
    }
  }

  fun CommonPath.WindingType.toFillRule() = when (this) {
    CommonPath.WindingType.EVEN_ODD -> Context2d.FillRule.EVENODD
    CommonPath.WindingType.NON_ZERO -> Context2d.FillRule.NONZERO
  }

  fun Context2d.applyStrokeData(strokeData: StrokeData) {
    Do exhaustive when (strokeData) {
      is StrokeData.Basic -> {
        setLineWidth(strokeData.lineWidth.toDouble())
        setLineCap(strokeData.endCap.toCanvasLineCap())
        setLineJoin(strokeData.lineJoin.toCanvasLineJoin())
        setMiterLimit(strokeData.miterLimit.toDouble())
        setLineDash(strokeData.dashArray?.map(Float::toDouble)?.toDoubleArray() ?: DoubleArray(0))
        setLineDashOffset(strokeData.dashPhase.toDouble())
      }
    }
  }

  fun StrokeData.Basic.CapType.toCanvasLineCap(): Context2d.LineCap {
    return when (this) {
      StrokeData.Basic.CapType.ROUND -> Context2d.LineCap.ROUND
      StrokeData.Basic.CapType.SQUARE -> Context2d.LineCap.SQUARE
      StrokeData.Basic.CapType.BUTT -> Context2d.LineCap.BUTT
    }
  }

  fun StrokeData.Basic.JoinType.toCanvasLineJoin(): Context2d.LineJoin {
    return when (this) {
      StrokeData.Basic.JoinType.ROUND -> Context2d.LineJoin.ROUND
      StrokeData.Basic.JoinType.BEVEL -> Context2d.LineJoin.BEVEL
      StrokeData.Basic.JoinType.MITER -> Context2d.LineJoin.MITER
    }
  }

  fun AlphaCompositeRule.toContext2dRule(): Context2d.CompositeOperationType = when (this) {
    AlphaCompositeRule.SRC_OVER -> Context2d.CompositeOperationType.SRC_OVER
    AlphaCompositeRule.DST_OVER -> Context2d.CompositeOperationType.DST_OVER
    AlphaCompositeRule.SRC_IN -> Context2d.CompositeOperationType.SRC_IN
    AlphaCompositeRule.DST_IN -> Context2d.CompositeOperationType.DST_IN
    AlphaCompositeRule.SRC_OUT -> Context2d.CompositeOperationType.SRC_OUT
    AlphaCompositeRule.DST_OUT -> Context2d.CompositeOperationType.DST_OUT
    AlphaCompositeRule.SRC_ATOP -> Context2d.CompositeOperationType.SRC_ATOP
    AlphaCompositeRule.DST_ATOP -> Context2d.CompositeOperationType.DST_ATOP
    AlphaCompositeRule.XOR -> Context2d.CompositeOperationType.XOR
    AlphaCompositeRule.SRC,
    AlphaCompositeRule.CLEAR,
    AlphaCompositeRule.DST,
    -> Context2d.CompositeOperationType.SRC_OVER.also {
      logger.info { "Missing implementation for $this, applying source-over" }
    }
  }

  fun Short.toFontFaceName(): String = "serverFont$this"

  // todo: rename
  fun Int.argbIntToRgbaString(): RGBA {
    val colorValue = this

    val b = colorValue and 0xFF
    val g = (colorValue ushr 8) and 0xFF
    val r = (colorValue ushr 16) and 0xFF
    val a = (colorValue ushr 24) and 0xFF

    return RGBA(r, g, b, a)
  }
}

