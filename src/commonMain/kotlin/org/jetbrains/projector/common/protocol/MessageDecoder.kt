package org.jetbrains.projector.common.protocol

import org.jetbrains.projector.common.protocol.handshake.ProtocolType

interface MessageDecoder<in InType, out OutType> {

  fun decode(message: InType): OutType

  val protocolType: ProtocolType
}
