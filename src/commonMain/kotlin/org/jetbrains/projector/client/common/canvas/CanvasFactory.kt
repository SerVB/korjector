package org.jetbrains.projector.client.common.canvas

import com.soywiz.korge.view.Image
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launch
import com.soywiz.korio.lang.*
import com.soywiz.korio.stream.openAsync
import com.soywiz.krypto.encoding.fromBase64
import mainStage

val USE_NATIVE_IMAGE = Environment["USE_NATIVE_IMAGE"] == "true"

object CanvasFactory {
  fun create(): Canvas = KorgeCanvas(Image(NativeImageOrBitmap32(42, 42, USE_NATIVE_IMAGE)))
  fun createImageSource(pngBase64: String, onLoad: (Canvas.ImageSource) -> Unit) {
    mainStage.launch {
      val bmp = pngBase64.fromBase64().openAsync().readBitmap()
      onLoad(KorgeCanvas.DomImageSource(bmp))
    }
  }

  fun createEmptyImageSource(onLoad: (Canvas.ImageSource) -> Unit) {
    onLoad(KorgeCanvas.DomImageSource(NativeImageOrBitmap32(20, 20, USE_NATIVE_IMAGE)))
  }
}
