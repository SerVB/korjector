package org.jetbrains.projector.common.protocol.toClient

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.projector.common.protocol.data.*

@Serializable
sealed class ServerWindowEvent

@Serializable
sealed class ServerWindowPaintEvent : ServerWindowEvent()

@Serializable
sealed class ServerWindowToDoPaintEvent : ServerWindowPaintEvent()

@Serializable
sealed class ServerWindowStateEvent : ServerWindowEvent()

@Serializable
sealed class ServerWindowToDoStateEvent : ServerWindowStateEvent()

@Serializable
@SerialName("a")
data class ServerPaintArcEvent(
  @SerialName("a")
  val paintType: PaintType,
  @SerialName("b")
  val x: Int,
  @SerialName("c")
  val y: Int,
  @SerialName("d")
  val width: Int,
  @SerialName("e")
  val height: Int,
  @SerialName("f")
  val startAngle: Int,
  @SerialName("g")
  val arcAngle: Int,
) : ServerWindowToDoPaintEvent()

@Serializable
@SerialName("b")
data class ServerPaintOvalEvent(
  @SerialName("a")
  val paintType: PaintType,
  @SerialName("b")
  val x: Int,
  @SerialName("c")
  val y: Int,
  @SerialName("d")
  val width: Int,
  @SerialName("e")
  val height: Int,
) : ServerWindowPaintEvent()

@Serializable
@SerialName("c")
data class ServerPaintRoundRectEvent(
  @SerialName("a")
  val paintType: PaintType,
  @SerialName("b")
  val x: Int,
  @SerialName("c")
  val y: Int,
  @SerialName("d")
  val width: Int,
  @SerialName("e")
  val height: Int,
  @SerialName("f")
  val arcWidth: Int,
  @SerialName("g")
  val arcHeight: Int,
) : ServerWindowPaintEvent()

@Serializable
@SerialName("d")
data class ServerPaintRectEvent(
  @SerialName("a")
  val paintType: PaintType,
  @SerialName("b")
  val x: Double,
  @SerialName("c")
  val y: Double,
  @SerialName("d")
  val width: Double,
  @SerialName("e")
  val height: Double,
) : ServerWindowPaintEvent()

@Serializable
@SerialName("e")
data class ServerDrawLineEvent(
  @SerialName("a")
  val x1: Int,
  @SerialName("b")
  val y1: Int,
  @SerialName("c")
  val x2: Int,
  @SerialName("d")
  val y2: Int,
) : ServerWindowPaintEvent()

@Serializable
@SerialName("f")
data class ServerCopyAreaEvent(
  @SerialName("a")
  val x: Int,
  @SerialName("b")
  val y: Int,
  @SerialName("c")
  val width: Int,
  @SerialName("d")
  val height: Int,
  @SerialName("e")
  val dx: Int,
  @SerialName("f")
  val dy: Int,
) : ServerWindowPaintEvent()

@Serializable
@SerialName("g")
data class ServerSetFontEvent(
  @SerialName("a")
  val fontId: Short? = null,
  @SerialName("b")
  val fontSize: Int,
  @SerialName("c")
  val ligaturesOn: Boolean = false,
) : ServerWindowStateEvent()

@Serializable
@SerialName("h")
data class ServerSetClipEvent(
  @SerialName("a")
  val shape: CommonShape? = null,
) : ServerWindowStateEvent()

@Serializable
@SerialName("i")
data class ServerSetStrokeEvent(
  @SerialName("a")
  val strokeData: StrokeData,
) : ServerWindowStateEvent()

@Serializable
@SerialName("j")
object ServerDrawRenderedImageEvent : ServerWindowToDoPaintEvent()

@Serializable
@SerialName("k")
object ServerDrawRenderableImageEvent : ServerWindowToDoPaintEvent()

@Serializable
@SerialName("l")
data class ServerDrawImageEvent(
  @SerialName("a")
  val imageId: ImageId,
  @SerialName("b")
  val imageEventInfo: ImageEventInfo,
) : ServerWindowPaintEvent()

@Serializable
@SerialName("m")
data class ServerDrawStringEvent(
  @SerialName("a")
  val str: String,
  @SerialName("b")
  val x: Double,
  @SerialName("c")
  val y: Double,
  @SerialName("d")
  val desiredWidth: Double,
) : ServerWindowPaintEvent()

@Serializable
@SerialName("n")
data class ServerPaintPolygonEvent(
  @SerialName("a")
  val paintType: PaintType,
  @SerialName("b")
  val points: List<org.jetbrains.projector.common.protocol.data.Point> = emptyList(),  // todo: remove default after https://github.com/Kotlin/kotlinx.serialization/issues/806
) : ServerWindowPaintEvent()

@Serializable
@SerialName("o")
data class ServerDrawPolylineEvent(
  @SerialName("a")
  val points: List<org.jetbrains.projector.common.protocol.data.Point> = emptyList(),  // todo: remove default after https://github.com/Kotlin/kotlinx.serialization/issues/806
) : ServerWindowPaintEvent()

@Serializable
@SerialName("p")
data class ServerSetTransformEvent(
  @SerialName("a")
  val tx: List<Double> = emptyList(),  // todo: remove default after https://github.com/Kotlin/kotlinx.serialization/issues/806
) : ServerWindowStateEvent()

@Serializable
@SerialName("q")
data class ServerPaintPathEvent(
  @SerialName("a")
  val paintType: PaintType,
  @SerialName("b")
  val path: CommonPath,
) : ServerWindowPaintEvent()

@Serializable
@SerialName("r")
data class ServerSetCompositeEvent(
  @SerialName("a")
  val composite: CommonComposite,
) : ServerWindowStateEvent()

@Serializable
@SerialName("s")
data class ServerSetPaintEvent(
  @SerialName("a")
  val paint: PaintValue,
) : ServerWindowStateEvent()

@Serializable
@SerialName("t")
data class ServerSetUnknownStrokeEvent(
  @SerialName("a")
  val className: String,
) : ServerWindowToDoStateEvent()
