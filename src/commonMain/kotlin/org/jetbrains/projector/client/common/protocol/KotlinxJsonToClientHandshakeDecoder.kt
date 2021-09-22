package org.jetbrains.projector.client.common.protocol

import org.jetbrains.projector.common.protocol.handshake.KotlinxJsonHandshakeEventSerializer
import org.jetbrains.projector.common.protocol.handshake.ProtocolType
import org.jetbrains.projector.common.protocol.handshake.ToClientHandshakeEvent

object KotlinxJsonToClientHandshakeDecoder :
  org.jetbrains.projector.common.protocol.MessageDecoder<ByteArray, ToClientHandshakeEvent> {

  override val protocolType = ProtocolType.KOTLINX_JSON

  @OptIn(ExperimentalStdlibApi::class)
  override fun decode(message: ByteArray): ToClientHandshakeEvent {
    val string = message.decodeToString()

    return KotlinxJsonHandshakeEventSerializer.deserializeToClientEvent(string)
  }
}
