package org.jetbrains.projector.common.misc

import org.jetbrains.projector.common.protocol.data.StrokeData

object Defaults {

  const val FONT_SIZE = 12

  const val FOREGROUND_COLOR_ARGB = 0xFF_00_00_00.toInt()
  const val BACKGROUND_COLOR_ARGB = 0xFF_FF_FF_FF.toInt()

  val STROKE =
    StrokeData.Basic(  // from Graphics2D "Default Rendering Attributes" java doc
      lineWidth = 1.0f,
      endCap = StrokeData.Basic.CapType.SQUARE,
      lineJoin = StrokeData.Basic.JoinType.MITER,
      miterLimit = 10.0f,
      dashArray = null,
      dashPhase = 0.0f
    )
}
