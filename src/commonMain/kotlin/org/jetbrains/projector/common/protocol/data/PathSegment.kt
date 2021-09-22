package org.jetbrains.projector.common.protocol.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class PathSegment {

  @Serializable
  @SerialName("a")
  data class MoveTo(
    @SerialName("a")
    val point: Point,
  ) : PathSegment()

  @Serializable
  @SerialName("b")
  data class LineTo(
    @SerialName("a")
    val point: Point,
  ) : PathSegment()

  @Serializable
  @SerialName("c")
  data class QuadTo(
    @SerialName("a")
    val point1: Point,
    @SerialName("b")
    val point2: Point,
  ) : PathSegment()

  @Serializable
  @SerialName("d")
  data class CubicTo(
    @SerialName("a")
    val point1: Point,
    @SerialName("b")
    val point2: Point,
    @SerialName("c")
    val point3: Point,
  ) : PathSegment()

  @Serializable
  @SerialName("e")
  object Close : PathSegment()
}
