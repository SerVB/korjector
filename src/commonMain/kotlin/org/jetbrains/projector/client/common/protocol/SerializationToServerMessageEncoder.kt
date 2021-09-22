package org.jetbrains.projector.client.common.protocol

import org.jetbrains.projector.common.protocol.handshake.ProtocolType
import org.jetbrains.projector.common.protocol.toServer.KotlinxJsonClientEventSerializer
import org.jetbrains.projector.common.protocol.toServer.ToServerMessageEncoder
import org.jetbrains.projector.common.protocol.toServer.ToServerMessageType
import org.jetbrains.projector.common.protocol.toServer.ToServerTransferableType

object SerializationToServerMessageEncoder : ToServerMessageEncoder {
  override val protocolType = ProtocolType.KOTLINX_JSON

  override fun encode(message: ToServerMessageType): ToServerTransferableType {
    return KotlinxJsonClientEventSerializer.serializeList(message)
  }
}
