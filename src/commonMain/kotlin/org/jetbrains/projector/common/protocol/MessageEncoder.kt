package org.jetbrains.projector.common.protocol

import org.jetbrains.projector.common.protocol.handshake.ProtocolType

interface MessageEncoder<in InType, out OutType> {

  fun encode(message: InType): OutType

  val protocolType: ProtocolType
}
