package org.jetbrains.projector.common.protocol.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class PaintValue {

  @Serializable
  @SerialName("a")
  data class Color(
    @SerialName("a")
    val argb: Int,
  ) : PaintValue()

  @Serializable
  @SerialName("b")
  data class Gradient(
    @SerialName("a")
    val p1: Point,
    @SerialName("b")
    val p2: Point,
    @SerialName("c")
    val argb1: Int,
    @SerialName("d")
    val argb2: Int,
  ) : PaintValue()

  @Serializable
  @SerialName("c")
  data class Unknown(
    @SerialName("a")
    val info: String,
  ) : PaintValue()
}
