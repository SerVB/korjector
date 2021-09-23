package org.jetbrains.projector.client.korge.input

import org.jetbrains.projector.common.protocol.toServer.KeyModifier
import org.jetbrains.projector.common.protocol.toServer.MouseModifier

class SpecialKeysState {

  var isAltEnabled = false
  var isCtrlEnabled = false
  var isShiftEnabled = false

  val keyModifiers: Set<KeyModifier>
    get() {
      val answer = mutableSetOf<KeyModifier>()

      if (isAltEnabled) {
        answer.add(KeyModifier.ALT_KEY)
      }
      if (isCtrlEnabled) {
        answer.add(KeyModifier.CTRL_KEY)
      }
      if (isShiftEnabled) {
        answer.add(KeyModifier.SHIFT_KEY)
      }

      return answer
    }

  val mouseModifiers: Set<MouseModifier>
    get() {
      val answer = mutableSetOf<MouseModifier>()

      if (isAltEnabled) {
        answer.add(MouseModifier.ALT_KEY)
      }
      if (isCtrlEnabled) {
        answer.add(MouseModifier.CTRL_KEY)
      }
      if (isShiftEnabled) {
        answer.add(MouseModifier.SHIFT_KEY)
      }

      return answer
    }
}
