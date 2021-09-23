package org.jetbrains.projector.client.common.canvas

import com.soywiz.korge.view.Image
import com.soywiz.korim.bitmap.Bitmap32
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launch
import com.soywiz.korio.stream.openAsync
import com.soywiz.krypto.encoding.fromBase64
import mainStage

object CanvasFactory {
  fun create(): Canvas = KorgeCanvas(Image(Bitmap32(42, 42, Colors.MAGENTA)))
  fun createImageSource(pngBase64: String, onLoad: (Canvas.ImageSource) -> Unit) {
    mainStage.launch {
      val bmp = pngBase64.fromBase64().openAsync().readBitmap()
      onLoad(KorgeCanvas.DomImageSource(bmp))
    }
  }

  fun createEmptyImageSource(onLoad: (Canvas.ImageSource) -> Unit) {
    onLoad(KorgeCanvas.DomImageSource(Bitmap32(20, 20, Colors.MAGENTA)))
  }
}
