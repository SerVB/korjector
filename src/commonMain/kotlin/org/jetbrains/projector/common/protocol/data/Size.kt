package org.jetbrains.projector.common.protocol.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonIntSize(
  @SerialName("a")
  val width: Int,
  @SerialName("b")
  val height: Int,
)

@Serializable
data class CommonIntRectangle(
  @SerialName("a")
  val x: Int,
  @SerialName("b")
  val y: Int,
  @SerialName("c")
  val width: Int,
  @SerialName("d")
  val height: Int,
)
