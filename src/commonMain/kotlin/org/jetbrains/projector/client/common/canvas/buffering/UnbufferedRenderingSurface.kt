package org.jetbrains.projector.client.common.canvas.buffering

import org.jetbrains.projector.client.common.canvas.Canvas
import org.jetbrains.projector.client.common.canvas.Extensions.resizeSavingImage
import org.jetbrains.projector.client.common.canvas.KorgeCanvas

class UnbufferedRenderingSurface(override val canvas: Canvas) : RenderingSurface {

  override var scalingRatio: Double = 1.0

  override fun setBounds(width: Int, height: Int) {
    canvas.resizeSavingImage(width, height)
  }

  override fun flush() {
    ++(canvas as KorgeCanvas).myCanvas.bitmap.bmp.contentVersion
  }
}
