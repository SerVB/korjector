package org.jetbrains.projector.common.statistics

sealed class StatsValue<ValueType : Number> {

  abstract fun add(extra: ValueType)

  abstract fun reset(): ValueType
}

class DoubleValue : StatsValue<Double>() {

  private var value: Double = 0.0

  override fun add(extra: Double) {
    value += extra
  }

  override fun reset(): Double {
    return value.also { value = 0.0 }
  }
}

class LongValue : StatsValue<Long>() {

  private var value: Long = 0

  override fun add(extra: Long) {
    value += extra
  }

  override fun reset(): Long {
    return value.also { value = 0 }
  }
}
