package org.jetbrains.projector.common.protocol.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class StrokeData {

  @Serializable
  @SerialName("a")
  data class Basic(
    @SerialName("a")
    val lineWidth: Float,
    @SerialName("b")
    val lineJoin: StrokeData.Basic.JoinType,
    @SerialName("c")
    val endCap: StrokeData.Basic.CapType,
    @SerialName("d")
    val miterLimit: Float,
    @SerialName("e")
    val dashPhase: Float,
    @SerialName("f")
    val dashArray: List<Float>? = null,
  ) : StrokeData() {

    @Serializable
    enum class JoinType {

      @SerialName("a")
      MITER,

      @SerialName("b")
      ROUND,

      @SerialName("c")
      BEVEL,
    }

    @Serializable
    enum class CapType {

      @SerialName("a")
      BUTT,

      @SerialName("b")
      ROUND,

      @SerialName("c")
      SQUARE,
    }
  }
}
