package org.jetbrains.projector.common.protocol.toServer

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

object KotlinxJsonClientEventSerializer {

  private val json = Json {}

  private val serializer = ListSerializer(ClientEvent.serializer())

  fun serializeList(msg: List<ClientEvent>): String = json.encodeToString(serializer, msg)
  fun deserializeList(data: String): List<ClientEvent> = json.decodeFromString(serializer, data)

  fun deserializeFromRelay(data: String): RelayControlEvent =
    json.decodeFromString(RelayControlEvent.serializer(), data)
}
