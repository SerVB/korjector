package org.jetbrains.projector.common.protocol.compress

import org.jetbrains.projector.common.protocol.handshake.CompressionType

class NotDecompressor<DataType> : MessageDecompressor<DataType> {

  override fun decompress(data: DataType) = data

  override val compressionType = CompressionType.NONE
}
