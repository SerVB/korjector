package org.jetbrains.projector.common.protocol.toServer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class RelayControlEvent

@Serializable
@SerialName("greeting")
object GreetingControlEvent : RelayControlEvent()

@Serializable
@SerialName("client-in")
data class ClientInControlEvent(
  val id: String
) : RelayControlEvent()

@Serializable
@SerialName("client-out")
data class ClientOutControlEvent(
  val id: String
) : RelayControlEvent()
