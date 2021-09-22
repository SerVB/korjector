package org.jetbrains.projector.client.common.canvas

/**
 *   class JvmCanvas(private val myPanel: JPanel) : Canvas {
 *     override val context2d: Context2d = JvmContext2d(myPanel.graphics)
 *   }
 *
 *   class JvmContext2d(private val myGraphics: Graphics) : Context2d {
 *     override fun clearRect(x: Double, y: Double, w: Double, h: Double) {
 *       myGraphics.clearRect(x, y, w, h)
 *     }
 *   }
 *
 *   class AndroidCanvas(private val nativeCanvas: Canvas) : Canvas {
 *     override val context2d: Context2d = AndroidContext2d(nativeCanvas)
 *   }
 *
 *   class AndroidContext2d(private val myNativeCanvas: Canvas) : Context2d {
 *     override fun clearRect(x: Double, y: Double, w: Double, h: Double) {
 *       myNativeCanvas.clipRect(x, y, w, h)
 *       myNativeCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
 *     }
 *   }
 */

interface Canvas {
  val context2d: Context2d
  var width: Int
  var height: Int
  var fontVariantLigatures: String
    get() = ""
    set(value) {}
  val imageSource: ImageSource

  fun takeSnapshot(): Snapshot

  interface ImageSource {
    fun isEmpty(): Boolean
  }

  // Unchangeable copy of canvas data
  interface Snapshot : ImageSource
}
