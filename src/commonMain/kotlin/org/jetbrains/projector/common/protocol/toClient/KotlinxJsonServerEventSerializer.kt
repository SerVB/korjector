package org.jetbrains.projector.common.protocol.toClient

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

object KotlinxJsonServerEventSerializer {

  val jsonConfiguration: JsonBuilder.() -> Unit = {
    useArrayPolymorphism = true
    encodeDefaults = true  // this is needed because our manual json parser can't handle nulls instead of empty lists
  }
  private val json = Json(builderAction = jsonConfiguration)

  private val serializer = ListSerializer(ServerEvent.serializer())

  fun serializeList(msg: List<ServerEvent>): String = json.encodeToString(serializer, msg)
  fun deserializeList(data: String): List<ServerEvent> = json.decodeFromString(serializer, data)
}
