package org.jetbrains.projector.common.protocol.toClient

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.projector.common.protocol.data.CommonRectangle
import org.jetbrains.projector.common.protocol.data.CursorType

@Serializable
enum class WindowType {
  @SerialName("a")
  WINDOW,

  @SerialName("b")
  POPUP,

  @SerialName("c")
  IDEA_WINDOW
}

@Serializable
data class WindowData(
  @SerialName("a")
  val id: Int,
  @SerialName("b")
  val title: String? = null,
  @SerialName("c")
  val icons: List<org.jetbrains.projector.common.protocol.data.ImageId>? = null,
  @SerialName("d")
  val isShowing: Boolean,
  /** Big value means front. */
  @SerialName("e")
  val zOrder: Int,
  @SerialName("f")
  val bounds: CommonRectangle,
  /** Null means no change. */
  @SerialName("g")
  val cursorType: CursorType? = null,
  @SerialName("h")
  val resizable: Boolean,
  @SerialName("i")
  val modal: Boolean,
  @SerialName("j")
  val undecorated: Boolean,
  @SerialName("k")
  val windowType: WindowType,
  /**
   * If the window has a header on the host, its sizes are included in the window bounds.
   * The client header is drawn above the window, outside its bounds. At the same time,
   * the coordinates of the contents of the window come taking into account the size
   * of the header. As a result, on client an empty space is obtained between header
   * and the contents of the window. To get rid of this, we transfer the height of the system
   * window header and if it > 0, we draw the heading not over the window but inside
   * the window's bounds, filling in the empty space.
   * If the window has not a header on the host, headerHeight == null.
   */
  @SerialName("l")
  val headerHeight: Int? = null,
)
