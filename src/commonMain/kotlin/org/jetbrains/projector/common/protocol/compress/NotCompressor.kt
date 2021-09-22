package org.jetbrains.projector.common.protocol.compress

import org.jetbrains.projector.common.protocol.handshake.CompressionType

class NotCompressor<DataType> : MessageCompressor<DataType> {

  override fun compress(data: DataType) = data

  override val compressionType = CompressionType.NONE
}
