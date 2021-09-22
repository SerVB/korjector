package org.jetbrains.projector.common.protocol.toServer

import org.jetbrains.projector.common.protocol.MessageDecoder
import org.jetbrains.projector.common.protocol.MessageEncoder

typealias ToServerMessageType = List<ClientEvent>
typealias ToServerTransferableType = String

interface ToServerMessageDecoder :
    MessageDecoder<ToServerTransferableType, ToServerMessageType>  // implement on the server side

interface ToServerMessageEncoder :
    MessageEncoder<ToServerMessageType, ToServerTransferableType>  // implement on the client side
