package org.jetbrains.projector.client.korge.window

import com.soywiz.klogger.Logger
import com.soywiz.korge.input.cursor
import com.soywiz.korge.view.image
import com.soywiz.korge.view.xy
import com.soywiz.korgw.GameWindow
import com.soywiz.korim.bitmap.*
import mainStage
import org.jetbrains.projector.client.common.DrawEvent
import org.jetbrains.projector.client.common.SingleRenderingSurfaceProcessor
import org.jetbrains.projector.client.common.canvas.*
import org.jetbrains.projector.client.common.canvas.buffering.createRenderingSurface
import org.jetbrains.projector.client.common.misc.ImageCacher
import org.jetbrains.projector.client.common.misc.ParamsProvider
import org.jetbrains.projector.client.korge.state.ClientAction
import org.jetbrains.projector.client.korge.state.ClientStateMachine
import org.jetbrains.projector.client.korge.state.LafListener
import org.jetbrains.projector.client.korge.state.ProjectorUI
import org.jetbrains.projector.common.protocol.data.CommonRectangle
import org.jetbrains.projector.common.protocol.data.CursorType
import org.jetbrains.projector.common.protocol.toClient.WindowData
import org.jetbrains.projector.common.protocol.toClient.WindowType
import org.jetbrains.projector.common.protocol.toServer.ClientWindowCloseEvent
import org.jetbrains.projector.common.protocol.toServer.ClientWindowMoveEvent
import org.jetbrains.projector.common.protocol.toServer.ClientWindowResizeEvent
import org.jetbrains.projector.common.protocol.toServer.ResizeDirection
import kotlin.math.roundToInt

interface Positionable {

  val bounds: CommonRectangle
  val zIndex: Int
}

class Window(windowData: WindowData, private val stateMachine: ClientStateMachine, imageCacher: ImageCacher) :
  LafListener, Positionable {

  val id = windowData.id

  val pendingDrawEvents = ArrayDeque<DrawEvent>()
  val newDrawEvents = ArrayDeque<DrawEvent>()

  var title: String? = null
    set(value) {
      field = value
      header?.title = value
    }

  var modal: Boolean = windowData.modal

  var isShowing: Boolean = false
    set(value) {
      header?.visible = value
//      border.visible = value  // todo

      if (field == value) {
        return
      }
      field = value
      canvas.visible = value
    }

  //public only for speculative typing.
  val canvas = createCanvas()
  private val renderingSurface = createRenderingSurface(canvas)

  private var header: WindowHeader? = null
  private var headerVerticalPosition: Double = 0.0
  private var headerHeight: Double = 0.0
//  private val border = WindowBorder(windowData.resizable)  // todo

  private val commandProcessor = SingleRenderingSurfaceProcessor(renderingSurface, imageCacher)

  override var bounds: CommonRectangle = CommonRectangle(0.0, 0.0, 0.0, 0.0)
    set(value) {
      if (field == value) {
        return
      }
      field = value
      applyBounds()
    }

  override var zIndex: Int = 0
    set(value) {
      if (field != value) {
        field = value
//        canvas.style.zIndex = "$zIndex"  // todo
        header?.zIndex = zIndex
//        border.zIndex = zIndex - 1  // todo
      }
    }

  var cursorType: CursorType = CursorType.DEFAULT
    set(value) {
      if (field != value) {
        field = value
        canvas.cursor = value.toKorgeCursor()
      }
    }

  init {
    applyBounds()

    if (windowData.windowType == WindowType.IDEA_WINDOW || windowData.windowType == WindowType.POPUP) {
//      canvas.style.border = "none"  // todo
    } else if (windowData.windowType == WindowType.WINDOW) {
      if (windowData.undecorated) {
//        canvas.style.border = ProjectorUI.borderStyle  // todo
      } else {
        // If the window has a header on the host, its sizes are included in the window bounds.
        // The client header is drawn above the window, outside its bounds. At the same time,
        // the coordinates of the contents of the window come taking into account the size
        // of the header. As a result, on client an empty space is obtained between header
        // and the contents of the window. To get rid of this, we transfer the height of the system
        // window header and if it > 0, we draw the heading not over the window but inside
        // the window's bounds, filling in the empty space.

        header = WindowHeader(mainStage, windowData.title)
        header!!.undecorated = windowData.undecorated
        header!!.onMove = ::onMove
        header!!.onClose = ::onClose

        headerVerticalPosition = when (windowData.headerHeight) {
          0, null -> ProjectorUI.headerHeight
          else -> 0.0
        }

        headerHeight = when (windowData.headerHeight) {
          0, null -> ProjectorUI.headerHeight
          else -> windowData.headerHeight.toDouble()
        }

        // todo
//        canvas.style.borderBottom = ProjectorUI.borderStyle
//        canvas.style.borderLeft = ProjectorUI.borderStyle
//        canvas.style.borderRight = ProjectorUI.borderStyle
//        canvas.style.borderRadius = "0 0 ${ProjectorUI.borderRadius}px ${ProjectorUI.borderRadius}px"
      }
    }

    if (windowData.resizable) {
//      border.onResize = ::onResize  // todo
    }
  }

  override fun lookAndFeelChanged() {
//    if (header != null) {  // todo
//      canvas.style.borderBottom = ProjectorUI.borderStyle
//      canvas.style.borderLeft = ProjectorUI.borderStyle
//      canvas.style.borderRight = ProjectorUI.borderStyle
//      canvas.style.borderRadius = "0 0 ${ProjectorUI.borderRadius}px ${ProjectorUI.borderRadius}px"
//    }
//    else if (canvas.style.border != "none") {
//      canvas.style.border = ProjectorUI.borderStyle
//    }

    header?.lookAndFeelChanged()
//    border.lookAndFeelChanged()  // todo
  }

  fun contains(x: Int, y: Int): Boolean {
    return bounds.contains(x, y) || header?.bounds?.contains(x, y) ?: false  // todo: deligate to border
  }

  fun onMouseDown(x: Int, y: Int): DragEventsInterceptor? {
    return /*border.onMouseDown(x, y) ?:*/ header?.onMouseDown(x, y)  // todo
  }

  fun onMouseClick(x: Int, y: Int): DragEventsInterceptor? {
    return /*border.onMouseClick(x, y) ?:*/ header?.onMouseClick(x, y)  // todo
  }

  private fun onResize(deltaX: Int, deltaY: Int, direction: ResizeDirection) {
    stateMachine.fire(ClientAction.AddEvent(ClientWindowResizeEvent(id, deltaX, deltaY, direction)))
  }

  private fun onMove(deltaX: Int, deltaY: Int) {
    stateMachine.fire(ClientAction.AddEvent(ClientWindowMoveEvent(id, deltaX, deltaY)))
  }

  private fun onClose() {
    stateMachine.fire(ClientAction.AddEvent(ClientWindowCloseEvent(id)))
  }

  private fun createCanvas() = mainStage.image(NativeImageOrBitmap32(42, 42, USE_NATIVE_IMAGE)).apply {
    visible = isShowing
  }

  fun applyBounds() {
    val userScalingRatio = ParamsProvider.USER_SCALING_RATIO
    val scalingRatio = ParamsProvider.SCALING_RATIO

    val clientBounds = CommonRectangle(
      bounds.x * userScalingRatio,
      bounds.y * userScalingRatio,
      bounds.width * userScalingRatio,
      bounds.height * userScalingRatio
    )

    if (header != null) {
      header!!.bounds = CommonRectangle(
        clientBounds.x,
        (bounds.y - headerVerticalPosition) * userScalingRatio,
        clientBounds.width,
        headerHeight * userScalingRatio
      )
    }

//    border.bounds = CommonRectangle(  // todo
//      clientBounds.x,
//      (bounds.y - headerVerticalPosition) * userScalingRatio,
//      clientBounds.width,
//      clientBounds.height + headerVerticalPosition * userScalingRatio
//    ).createExtended(ProjectorUI.borderThickness * userScalingRatio)

    canvas.xy(clientBounds.x, clientBounds.y)

    renderingSurface.scalingRatio = scalingRatio
    renderingSurface.setBounds(
      width = (bounds.width * scalingRatio).roundToInt(),
      height = (bounds.height * scalingRatio).roundToInt()
    )
  }

  fun dispose() {
    canvas.removeFromParent()
//    border.dispose()  // todo
    header?.dispose()
  }

  fun drawPendingEvents() {
    if (pendingDrawEvents.isNotEmpty()) {
      commandProcessor.processPending(pendingDrawEvents)
      renderingSurface.flush()
    }
    header?.draw()  // todo: do we need to draw it every time?
  }

  fun drawNewEvents() {
    val firstUnsuccessful = commandProcessor.processNew(newDrawEvents)
    renderingSurface.flush()
    header?.draw()  // todo: do we need to draw it every time?

    if (pendingDrawEvents.isNotEmpty()) {
      pendingDrawEvents.addAll(newDrawEvents)
    } else if (firstUnsuccessful != null) {
      if (pendingDrawEvents.isNotEmpty()) {
        logger.error { "Non empty pendingDrawEvents are handled by another branch, aren't they? This branch works only for empty." }
      }
      pendingDrawEvents.addAll(newDrawEvents.subList(firstUnsuccessful, newDrawEvents.size))
    }
    newDrawEvents.clear()
  }

  companion object {
    private val logger = Logger<Window>()

    private fun CursorType.toKorgeCursor(): GameWindow.Cursor? = when (this) {
      CursorType.DEFAULT -> GameWindow.Cursor.DEFAULT
      CursorType.CROSSHAIR -> GameWindow.Cursor.CROSSHAIR
      CursorType.TEXT -> GameWindow.Cursor.TEXT
      CursorType.WAIT -> GameWindow.Cursor.WAIT
      CursorType.SW_RESIZE -> GameWindow.Cursor.RESIZE_SOUTH_WEST
      CursorType.SE_RESIZE -> GameWindow.Cursor.RESIZE_SOUTH_EAST
      CursorType.NW_RESIZE -> GameWindow.Cursor.RESIZE_NORTH_WEST
      CursorType.NE_RESIZE -> GameWindow.Cursor.RESIZE_NORTH_EAST
      CursorType.N_RESIZE -> GameWindow.Cursor.RESIZE_NORTH
      CursorType.S_RESIZE -> GameWindow.Cursor.RESIZE_SOUTH
      CursorType.W_RESIZE -> GameWindow.Cursor.RESIZE_WEST
      CursorType.E_RESIZE -> GameWindow.Cursor.RESIZE_EAST
      CursorType.HAND -> GameWindow.Cursor.HAND
      CursorType.MOVE -> GameWindow.Cursor.MOVE
      CursorType.CUSTOM -> null // todo: the major use-case of "custom" is to hide the cursor in IDEA,
      //       so it's correct to use "null" for this, but need to support arbitrary cursors
    }
  }
}
