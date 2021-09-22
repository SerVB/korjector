package org.jetbrains.projector.client.korge.state

import org.jetbrains.projector.common.protocol.toClient.ServerWindowColorsEvent

interface LafListener {
  fun lookAndFeelChanged()
}

// Name of this class was chosen based on JBUI class.
object ProjectorUI {
  var windowHeaderActiveBackgroundArgb = 0xFFE6E6E6.toInt()
    private set

  var windowHeaderInactiveBackgroundArgb = 0xFFEDEDED.toInt()
    private set

  var windowActiveBorderArgb = 0xFFD5D5D5.toInt()
    private set

  var windowInactiveBorderArgb = 0xFFAAAAAA.toInt()
    private set

  var windowHeaderActiveTextArgb = 0xFF1A1A1A.toInt()
    private set

  var windowHeaderInactiveTextArgb = 0xFFDDDDDD.toInt()
    private set

//  var borderStyle = "1px solid ${windowActiveBorderArgb.argbIntToRgbaString()}"  // todo: port to korge
//    private set

  const val crossOffset = 4.0
  const val headerHeight = 28.0
  const val borderRadius = 8
  const val borderThickness = 8.0

  fun setColors(colors: ServerWindowColorsEvent.ColorsStorage) {
    windowHeaderActiveBackgroundArgb = colors.windowHeaderActiveBackground.argb
    windowHeaderInactiveBackgroundArgb = colors.windowHeaderInactiveBackground.argb
    windowActiveBorderArgb = colors.windowActiveBorder.argb
//    borderStyle = "1px solid ${windowActiveBorderArgb.argbIntToRgbaString()}"  // todo: port to korge
    windowInactiveBorderArgb = colors.windowInactiveBorder.argb
    windowHeaderActiveTextArgb = colors.windowHeaderActiveText.argb
    windowHeaderInactiveTextArgb = colors.windowHeaderInactiveText.argb
  }
}
