package org.jetbrains.projector.common.protocol.handshake

import kotlinx.serialization.builtins.ListSerializer
import org.jetbrains.projector.common.misc.compatibilityHash
import org.jetbrains.projector.common.protocol.toClient.ServerEvent
import org.jetbrains.projector.common.protocol.toServer.ClientEvent

val HANDSHAKE_VERSION = listOf(ToClientHandshakeEvent.serializer(), ToServerHandshakeEvent.serializer())
  .map { ListSerializer(it).descriptor.compatibilityHash }
  .reduce(Int::xor)

// Don't change order here: it's used to obtain readable "human id"
val handshakeVersionList = listOf(
  456250626,
)

val COMMON_VERSION = listOf(ServerEvent.serializer(), ClientEvent.serializer())
  .map { ListSerializer(it).descriptor.compatibilityHash }
  .reduce(Int::xor)

// Don't change order here: it's used to obtain readable "human id"
val commonVersionList = listOf(
  -1663032476,
  615706807,
  891030124,
  -1205505588,
  581264379,
  -625612891,
  -560999684,
  471600343,
  -215338327,
  1580903519,
  36358479,
  1670488062,
  -733798733,
  -1255984693,
)
