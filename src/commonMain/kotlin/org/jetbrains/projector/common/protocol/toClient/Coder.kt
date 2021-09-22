package org.jetbrains.projector.common.protocol.toClient

typealias ToClientMessageType = List<ServerEvent>
typealias ToClientTransferableType = ByteArray

interface ToClientMessageDecoder :
    org.jetbrains.projector.common.protocol.MessageDecoder<ToClientTransferableType, ToClientMessageType>  // implement on the client side

interface ToClientMessageEncoder :
    org.jetbrains.projector.common.protocol.MessageEncoder<ToClientMessageType, ToClientTransferableType>  // implement on the server side
