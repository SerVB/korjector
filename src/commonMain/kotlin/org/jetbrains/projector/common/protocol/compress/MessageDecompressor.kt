package org.jetbrains.projector.common.protocol.compress

import org.jetbrains.projector.common.protocol.handshake.CompressionType

interface MessageDecompressor<DataType> {

  fun decompress(data: DataType): DataType

  val compressionType: CompressionType
}
