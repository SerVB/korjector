package org.jetbrains.projector.client.common.protocol

import org.jetbrains.projector.common.protocol.MessageEncoder
import org.jetbrains.projector.common.protocol.handshake.KotlinxJsonHandshakeEventSerializer
import org.jetbrains.projector.common.protocol.handshake.ProtocolType
import org.jetbrains.projector.common.protocol.handshake.ToServerHandshakeEvent
import org.jetbrains.projector.common.protocol.toServer.ToServerTransferableType

object KotlinxJsonToServerHandshakeEncoder : MessageEncoder<ToServerHandshakeEvent, ToServerTransferableType> {

  override val protocolType = ProtocolType.KOTLINX_JSON

  override fun encode(message: ToServerHandshakeEvent): ToServerTransferableType {
    return KotlinxJsonHandshakeEventSerializer.serializeToServerEvent(message)
  }
}
