package org.jetbrains.projector.common.protocol.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class CommonComposite

@Serializable
@SerialName("a")
data class CommonAlphaComposite(
  @SerialName("a")
  val rule: AlphaCompositeRule,
  @SerialName("b")
  val alpha: Float,
) : CommonComposite()

@Serializable
enum class AlphaCompositeRule {
  @SerialName("a")
  SRC_OVER,

  @SerialName("b")
  DST_OVER,

  @SerialName("c")
  SRC_IN,

  @SerialName("d")
  CLEAR,

  @SerialName("e")
  SRC,

  @SerialName("f")
  DST,

  @SerialName("g")
  DST_IN,

  @SerialName("h")
  SRC_OUT,

  @SerialName("i")
  DST_OUT,

  @SerialName("j")
  SRC_ATOP,

  @SerialName("k")
  DST_ATOP,

  @SerialName("l")
  XOR,
}

@Serializable
@SerialName("b")
data class UnknownComposite(
  @SerialName("a")
  val message: String,
) : CommonComposite()
