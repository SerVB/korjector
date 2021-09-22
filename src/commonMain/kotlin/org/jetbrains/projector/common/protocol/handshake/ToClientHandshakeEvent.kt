package org.jetbrains.projector.common.protocol.handshake

import kotlinx.serialization.Serializable
import org.jetbrains.projector.common.protocol.toClient.ServerWindowColorsEvent

@Serializable
sealed class ToClientHandshakeEvent

@Serializable
data class ToClientHandshakeSuccessEvent(
    val toClientCompression: CompressionType,
    val toClientProtocol: ProtocolType,
    val toServerCompression: CompressionType,
    val toServerProtocol: ProtocolType,
    val fontDataHolders: List<org.jetbrains.projector.common.protocol.data.FontDataHolder>,
    val colors: ServerWindowColorsEvent.ColorsStorage? = null,
) : ToClientHandshakeEvent()

@Serializable
data class ToClientHandshakeFailureEvent(
    val reason: String,
) : ToClientHandshakeEvent()
