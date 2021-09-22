package org.jetbrains.projector.common.protocol.handshake

import kotlinx.serialization.Serializable

@Serializable
data class ToServerHandshakeEvent(
  val commonVersion: Int,
  val commonVersionId: Int,
  val token: String? = null,
  val clientDoesWindowManagement: Boolean,
  val displays: List<DisplayDescription>,
  val supportedToClientCompressions: List<CompressionType>,
  val supportedToClientProtocols: List<ProtocolType>,
  val supportedToServerCompressions: List<CompressionType>,
  val supportedToServerProtocols: List<ProtocolType>,
)

@Serializable
data class DisplayDescription(val x: Int, val y: Int, val width: Int, val height: Int, val scaleFactor: Double)
