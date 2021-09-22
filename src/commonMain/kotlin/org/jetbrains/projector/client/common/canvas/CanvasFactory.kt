package org.jetbrains.projector.client.common.canvas

import com.soywiz.korge.view.Image
import com.soywiz.korim.bitmap.Bitmap32
import com.soywiz.korim.color.Colors

object CanvasFactory {
  fun create(): Canvas = KorgeCanvas(Image(Bitmap32(42, 42, Colors.MAGENTA)))
  fun createImageSource(pngBase64: String, onLoad: (Canvas.ImageSource) -> Unit) {
    onLoad(KorgeCanvas.DomImageSource(Bitmap32(42, 42, Colors.MAGENTA)))  // todo
  }

  fun createEmptyImageSource(onLoad: (Canvas.ImageSource) -> Unit) {
    onLoad(KorgeCanvas.DomImageSource(Bitmap32(20, 20, Colors.MAGENTA)))
  }
}
