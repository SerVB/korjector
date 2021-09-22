package org.jetbrains.projector.common.protocol.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ImageData {

  @Serializable
  @SerialName("a")
  data class PngBase64(
    @SerialName("a")
    val pngBase64: String,
  ) : ImageData()

  @Serializable
  @SerialName("b")
  object Empty : ImageData()
}
