package org.jetbrains.projector.common.protocol.handshake

import kotlinx.serialization.json.Json

object KotlinxJsonHandshakeEventSerializer {

  private val json = Json {
    encodeDefaults = false
  }

  private val toClientSerializer = ToClientHandshakeEvent.serializer()
  private val toServerSerializer = ToServerHandshakeEvent.serializer()

  fun serializeToClientEvent(msg: ToClientHandshakeEvent): String = json.encodeToString(toClientSerializer, msg)
  fun deserializeToClientEvent(data: String): ToClientHandshakeEvent = json.decodeFromString(toClientSerializer, data)

  fun serializeToServerEvent(msg: ToServerHandshakeEvent): String = json.encodeToString(toServerSerializer, msg)
  fun deserializeToServerEvent(data: String): ToServerHandshakeEvent = json.decodeFromString(toServerSerializer, data)
}
