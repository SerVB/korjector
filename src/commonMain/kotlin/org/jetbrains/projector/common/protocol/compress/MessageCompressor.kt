package org.jetbrains.projector.common.protocol.compress

import org.jetbrains.projector.common.protocol.handshake.CompressionType

interface MessageCompressor<DataType> {

  fun compress(data: DataType): DataType

  val compressionType: CompressionType
}
