package org.jetbrains.projector.client.korge.misc

import com.soywiz.korim.font.Font
import com.soywiz.korim.font.TtfFont
import com.soywiz.krypto.encoding.fromBase64
import org.jetbrains.projector.client.common.canvas.Extensions.toFontFaceName
import org.jetbrains.projector.common.protocol.data.TtfFontData
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object FontFaceAppender {

  private var loadedFonts = 0
  private val fonts = mutableMapOf<Short, Font>()

  fun appendFontFaceToPage(fontId: Short, fontData: TtfFontData, onLoad: (Int) -> Unit) {
    val fontFaceName = fontId.toFontFaceName()
    val font = TtfFont(fontData.ttfBase64.fromBase64(), extName = fontFaceName)
    fonts[fontId] = font

    ++loadedFonts
    onLoad(loadedFonts)
  }

  fun getFont(fontId: Short): Font? = fonts[fontId]

  fun removeAppendedFonts() {
    fonts.clear()
    loadedFonts = 0
  }
}
