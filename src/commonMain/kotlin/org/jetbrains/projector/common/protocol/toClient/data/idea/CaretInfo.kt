package org.jetbrains.projector.common.protocol.toClient.data.idea

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.projector.common.protocol.data.Point

@Serializable
data class CaretInfo(
    @SerialName("a")
    val locationInWindow: Point,
)
