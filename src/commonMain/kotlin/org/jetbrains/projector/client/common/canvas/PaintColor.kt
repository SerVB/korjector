package org.jetbrains.projector.client.common.canvas

sealed class PaintColor {
  data class SolidColor(val argb: Int) : PaintColor() {
    constructor(argb: Long) : this(argb.toInt())
  }

  abstract class Gradient : PaintColor() {
    abstract fun addColorStop(offset: Double, argb: Int)
  }
}
