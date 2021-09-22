package org.jetbrains.projector.common.protocol.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ImageEventInfo {

  @Serializable
  @SerialName("a")
  data class Xy(
    @SerialName("a")
    val x: Int,
    @SerialName("b")
    val y: Int,
    @SerialName("c")
    val argbBackgroundColor: Int? = null,
  ) : ImageEventInfo()

  @Serializable
  @SerialName("b")
  data class XyWh(
    @SerialName("a")
    val x: Int,
    @SerialName("b")
    val y: Int,
    @SerialName("c")
    val width: Int,
    @SerialName("d")
    val height: Int,
    @SerialName("e")
    val argbBackgroundColor: Int? = null,
  ) : ImageEventInfo()

  @Serializable
  @SerialName("c")
  data class Ds(
    @SerialName("a")
    val dx1: Int,
    @SerialName("b")
    val dy1: Int,
    @SerialName("c")
    val dx2: Int,
    @SerialName("d")
    val dy2: Int,
    @SerialName("e")
    val sx1: Int,
    @SerialName("f")
    val sy1: Int,
    @SerialName("g")
    val sx2: Int,
    @SerialName("h")
    val sy2: Int,
    @SerialName("i")
    val argbBackgroundColor: Int? = null,
  ) : ImageEventInfo()

  @Serializable
  @SerialName("d")
  data class Transformed(
    @SerialName("a")
    val tx: List<Double> = emptyList(),  // todo: remove default after https://github.com/Kotlin/kotlinx.serialization/issues/806
  ) : ImageEventInfo()
}
