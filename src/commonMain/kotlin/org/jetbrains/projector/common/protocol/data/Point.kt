package org.jetbrains.projector.common.protocol.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Point(
  @SerialName("a")
  val x: Double,
  @SerialName("b")
  val y: Double,
)
