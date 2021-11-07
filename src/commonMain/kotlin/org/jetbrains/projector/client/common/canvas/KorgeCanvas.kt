package org.jetbrains.projector.client.common.canvas

import com.soywiz.korge.view.Image
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.color.Colors
import org.jetbrains.projector.client.common.canvas.Canvas.ImageSource
import org.jetbrains.projector.client.common.canvas.Canvas.Snapshot

class KorgeCanvas(val myCanvas: Image) : Canvas {

  override val context2d: Context2d = KorgeContext2d(myCanvas)

  override var width: Int
    get() = myCanvas.width.toInt()
    set(value) {
      myCanvas.bitmapSrc = NativeImageOrBitmap32(value, height, USE_NATIVE_IMAGE).slice()
    }

  override var height: Int
    get() = myCanvas.height.toInt()
    set(value) {
      myCanvas.bitmapSrc = NativeImageOrBitmap32(width, value, USE_NATIVE_IMAGE).slice()
    }

  override var fontVariantLigatures: String
    get() = "none"  // todo
    set(value) {
      // todo
    }
  override val imageSource: ImageSource = DomImageSource((myCanvas.bitmapSrc as BitmapSlice<Bitmap>).bmp)

  override fun takeSnapshot(): Snapshot {
    val copy = myCanvas.bitmap.extract()  // todo

    return object : DomImageSource(copy), Snapshot {}
  }

  open class DomImageSource(val canvasElement: Bitmap) : ImageSource {
    override fun isEmpty(): Boolean {
      return canvasElement.isEmpty()
    }
  }

  companion object {

    fun Bitmap.isEmpty(): Boolean = this.area == 0
  }
}
