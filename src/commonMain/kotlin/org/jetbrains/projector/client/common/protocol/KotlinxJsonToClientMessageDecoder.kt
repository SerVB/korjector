package org.jetbrains.projector.client.common.protocol

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.jetbrains.projector.common.protocol.handshake.ProtocolType
import org.jetbrains.projector.common.protocol.toClient.KotlinxJsonServerEventSerializer
import org.jetbrains.projector.common.protocol.toClient.ServerEvent
import org.jetbrains.projector.common.protocol.toClient.ToClientMessageDecoder
import org.jetbrains.projector.common.protocol.toClient.ToClientMessageType

object KotlinxJsonToClientMessageDecoder : ToClientMessageDecoder {

  override val protocolType = ProtocolType.KOTLINX_JSON

  private val json = Json(builderAction = KotlinxJsonServerEventSerializer.jsonConfiguration)

  private val serializer = ListSerializer(ServerEvent.serializer())

  override fun decode(message: ByteArray): ToClientMessageType {
    val string = message.decodeToString()

    return json.decodeFromString(serializer, string)
  }
}
