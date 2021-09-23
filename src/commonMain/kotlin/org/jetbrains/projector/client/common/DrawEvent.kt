package org.jetbrains.projector.client.common

import org.jetbrains.projector.common.protocol.toClient.ServerWindowPaintEvent
import org.jetbrains.projector.common.protocol.toClient.ServerWindowStateEvent

data class DrawEvent(
  val prerequisites: List<ServerWindowStateEvent>,
  val paintEvent: ServerWindowPaintEvent,
)
