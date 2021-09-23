package org.jetbrains.projector.client.korge

import mainStage
import org.jetbrains.projector.client.common.misc.ParamsProvider
import org.jetbrains.projector.client.korge.state.ClientStateMachine
import org.jetbrains.projector.common.protocol.data.CommonIntSize
import kotlin.math.roundToInt

class WindowSizeController(private val stateMachine: ClientStateMachine) {

  val currentSize: CommonIntSize
    get() {
      val userScalingRatio = ParamsProvider.USER_SCALING_RATIO

      return CommonIntSize(
        width = (mainStage.unscaledWidth / userScalingRatio).roundToInt(),
        height = (mainStage.unscaledHeight / userScalingRatio).roundToInt()
      )
    }

//  // todo: rewrite to korge
//  private fun handleResizeEvent(event: Event) {
//    stateMachine.fire(ClientAction.AddEvent(ClientResizeEvent(size = currentSize)))
//    stateMachine.fire(ClientAction.WindowResize)
//  }
//
//  fun addListener() {
//    window.addEventListener(RESIZE_EVENT_TYPE, ::handleResizeEvent)
//  }
//
//  fun removeListener() {
//    window.removeEventListener(RESIZE_EVENT_TYPE, ::handleResizeEvent)
//  }

  companion object {

    private const val RESIZE_EVENT_TYPE = "resize"
  }
}
