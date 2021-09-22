package org.jetbrains.projector.common.protocol.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class CommonShape

@Serializable
@SerialName("a")
data class CommonRectangle(
  @SerialName("a")
  val x: Double,
  @SerialName("b")
  val y: Double,
  @SerialName("c")
  val width: Double,
  @SerialName("d")
  val height: Double,
) : CommonShape() {

  fun contains(x: Int, y: Int) = this.x <= x && x < this.x + this.width && this.y <= y && y < this.y + this.height

  fun createExtended(extend: Double) = CommonRectangle(
    x - extend,
    y - extend,
    width + extend * 2,
    height + extend * 2
  )
}

@Serializable
@SerialName("b")
data class CommonPath(
  @SerialName("a")
  val segments: List<org.jetbrains.projector.common.protocol.data.PathSegment> = emptyList(),  // todo: remove default after https://github.com/Kotlin/kotlinx.serialization/issues/806
  @SerialName("b")
  val winding: CommonPath.WindingType,
) : CommonShape() {

  @Serializable
  enum class WindingType {

    @SerialName("a")
    EVEN_ODD,

    @SerialName("b")
    NON_ZERO,
  }
}
