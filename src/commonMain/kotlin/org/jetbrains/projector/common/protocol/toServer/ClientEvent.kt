package org.jetbrains.projector.common.protocol.toServer

import kotlinx.serialization.Serializable
import org.jetbrains.projector.common.protocol.data.*
import org.jetbrains.projector.common.protocol.handshake.DisplayDescription

enum class ResizeDirection {
  NW,
  SW,
  NE,
  SE,
  N,
  W,
  S,
  E
}

enum class MouseModifier {
  SHIFT_KEY,
  CTRL_KEY,
  ALT_KEY,
  META_KEY,
}

enum class KeyModifier {
  SHIFT_KEY,
  CTRL_KEY,
  ALT_KEY,
  META_KEY,
  REPEAT,
}

@Serializable
sealed class ClientEvent

@Serializable
data class ClientMouseEvent(
  /** From connection opening. */
  val timeStamp: Int,
  val windowId: Int,
  val x: Int,
  val y: Int,
  val button: Short,
  val clickCount: Int,
  val modifiers: Set<MouseModifier>,
  val mouseEventType: MouseEventType,
) : ClientEvent() {

  enum class MouseEventType {
    MOVE,
    DRAG,
    TOUCH_DRAG,
    DOWN,
    UP,
    CLICK,
    OUT,
  }
}

@Serializable
data class ClientWheelEvent(
  /** From connection opening. */
  val timeStamp: Int,
  val windowId: Int,
  val modifiers: Set<MouseModifier>,
  val mode: ScrollingMode,
  val x: Int,
  val y: Int,
  val deltaX: Double,
  val deltaY: Double,
) : ClientEvent() {

  enum class ScrollingMode {
    PIXEL,
    LINE,
    PAGE,
  }
}

@Serializable
data class ClientKeyEvent(
  /** From connection opening. */
  val timeStamp: Int,
  val char: Char,
  val code: VK,
  val location: KeyLocation,
  val modifiers: Set<KeyModifier>,
  val keyEventType: KeyEventType,
) : ClientEvent() {

  enum class KeyEventType {
    DOWN,
    UP,
  }

  enum class KeyLocation {
    STANDARD,
    LEFT,
    RIGHT,
    NUMPAD,
  }
}

@Serializable
data class ClientKeyPressEvent(
  /** From connection opening. */
  val timeStamp: Int,
  val char: Char,
  val modifiers: Set<KeyModifier>,
) : ClientEvent()

@Serializable
data class ClientRawKeyEvent(
  /** From connection opening. */
  val timeStamp: Int,
  val code: Int,
  val char: Char,
  val modifiers: Int,
  val location: Int,
  val keyEventType: RawKeyEventType,
) : ClientEvent() {

  enum class RawKeyEventType {
    DOWN,
    UP,
    TYPED
  }
}

@Serializable
data class ClientResizeEvent(val size: CommonIntSize) : ClientEvent()

@Serializable
data class ClientRequestImageDataEvent(val imageId: ImageId) :
  ClientEvent()

@Serializable
data class ClientClipboardEvent(
  val stringContent: String,  // TODO: support more types
) : ClientEvent()

@Serializable
data class ClientRequestPingEvent(
  /** From connection opening. */
  val clientTimeStamp: Int,
) : ClientEvent()

@Serializable
data class ClientSetKeymapEvent(
  val keymap: UserKeymap,
) : ClientEvent()

@Serializable
data class ClientOpenLinkEvent(
  val link: String,
) : ClientEvent()

@Serializable
data class ClientWindowMoveEvent(
  val windowId: Int,
  val deltaX: Int,
  val deltaY: Int,
) : ClientEvent()

// If delta is negative, we resizing to the left top
@Serializable
data class ClientWindowResizeEvent(
  val windowId: Int,
  val deltaX: Int,
  val deltaY: Int,
  val direction: ResizeDirection,
) : ClientEvent()

@Serializable
data class ClientWindowSetBoundsEvent(
  val windowId: Int,
  val bounds: CommonIntRectangle
) : ClientEvent()

@Serializable
data class ClientDisplaySetChangeEvent(
  val newDisplays: List<DisplayDescription>
) : ClientEvent()

@Serializable
data class ClientWindowCloseEvent(
  val windowId: Int,
) : ClientEvent()

@Serializable
data class ClientWindowInterestEvent(
  val windowId: Int,
  val isInterested: Boolean
) : ClientEvent()
