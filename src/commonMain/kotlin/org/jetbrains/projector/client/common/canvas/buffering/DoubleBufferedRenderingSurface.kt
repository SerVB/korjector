package org.jetbrains.projector.client.common.canvas.buffering

import org.jetbrains.projector.client.common.canvas.Canvas
import org.jetbrains.projector.client.common.canvas.CanvasFactory
import org.jetbrains.projector.client.common.canvas.Extensions.resizeSavingImage

class DoubleBufferedRenderingSurface(private val target: Canvas) : RenderingSurface {

  override var scalingRatio: Double = 1.0

  private val buffer = createBuffer()

  override val canvas: Canvas
    get() = buffer

  override fun setBounds(width: Int, height: Int) {
    buffer.resizeSavingImage(width, height)
    target.resizeSavingImage(width, height)
  }

  override fun flush() {
    if (buffer.width == 0 || buffer.height == 0) return

    target.context2d.drawImage(buffer.imageSource, 0.0, 0.0)
  }

  private fun createBuffer(): Canvas {
    return CanvasFactory.create()
      .apply {
        width = target.width
        height = target.height
      }
  }
}
