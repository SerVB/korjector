package org.jetbrains.projector.client.korge.protocol

import org.jetbrains.projector.client.common.misc.ParamsProvider
import org.jetbrains.projector.client.common.protocol.KotlinxJsonToClientMessageDecoder
import org.jetbrains.projector.client.common.protocol.SerializationToServerMessageEncoder
import org.jetbrains.projector.common.protocol.compress.MessageCompressor
import org.jetbrains.projector.common.protocol.compress.MessageDecompressor
import org.jetbrains.projector.common.protocol.compress.NotCompressor
import org.jetbrains.projector.common.protocol.compress.NotDecompressor
import org.jetbrains.projector.common.protocol.toClient.ToClientMessageDecoder
import org.jetbrains.projector.common.protocol.toServer.ToServerMessageEncoder

object SupportedTypesProvider {

  val supportedToClientDecompressors: List<MessageDecompressor<ByteArray>> = when (ParamsProvider.ENABLE_COMPRESSION) {
    true -> listOf(TODO())

    false -> listOf(NotDecompressor())
  }

  val supportedToClientDecoders: List<ToClientMessageDecoder> = listOf(
    KotlinxJsonToClientMessageDecoder,
  )

  val supportedToServerCompressors: List<MessageCompressor<String>> = listOf(
    NotCompressor(),
  )

  val supportedToServerEncoders: List<ToServerMessageEncoder> = listOf(
    SerializationToServerMessageEncoder,
  )
}
