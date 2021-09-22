package org.jetbrains.projector.client.common.canvas.buffering

import com.soywiz.korge.view.Image
import org.jetbrains.projector.client.common.canvas.Canvas
import org.jetbrains.projector.client.common.canvas.KorgeCanvas
import org.jetbrains.projector.client.common.misc.ParamsProvider

interface RenderingSurface {

  var scalingRatio: Double

  fun setBounds(width: Int, height: Int)

  val canvas: Canvas

  fun flush()
}

fun createRenderingSurface(canvas: Image) = when (ParamsProvider.DOUBLE_BUFFERING) {
  true -> DoubleBufferedRenderingSurface(KorgeCanvas(canvas))
  false -> UnbufferedRenderingSurface(KorgeCanvas(canvas))
}
