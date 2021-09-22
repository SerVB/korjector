package org.jetbrains.projector.common.protocol.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CursorType {
  @SerialName("a")
  DEFAULT,

  @SerialName("b")
  CROSSHAIR,

  @SerialName("c")
  TEXT,

  @SerialName("d")
  WAIT,

  @SerialName("e")
  SW_RESIZE,

  @SerialName("f")
  SE_RESIZE,

  @SerialName("g")
  NW_RESIZE,

  @SerialName("h")
  NE_RESIZE,

  @SerialName("i")
  N_RESIZE,

  @SerialName("j")
  S_RESIZE,

  @SerialName("k")
  W_RESIZE,

  @SerialName("l")
  E_RESIZE,

  @SerialName("m")
  HAND,

  @SerialName("n")
  MOVE,

  @SerialName("o")
  CUSTOM,
}
