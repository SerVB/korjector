package org.jetbrains.projector.common.statistics

class Average<ValueType : Number> private constructor(
  private val value: StatsValue<ValueType>,
  private val unit: String?,
  private val name: String,
  private val roundingStrategy: RoundingStrategy,
) {

  private var iterations: Long = 0

  fun add(measurement: ValueType) {
    // todo: synchronization is needed here
    ++iterations
    value.add(measurement)
  }

  fun reset(): ResetResult {
    // todo: synchronization is needed here
    val data = when (iterations) {
      0L -> Data.Empty

      else -> {
        val resetValue = value.reset()

        Data.Success(resetValue.toDouble() / iterations, iterations).also { iterations = 0 }
      }
    }

    return ResetResult(data)
  }

  inner class ResetResult(val data: Data) {

    fun generateString(separator: String): String =
      "$name average:$separator${data.generateString(roundingStrategy, unit, separator)}"
  }

  sealed class Data {

    abstract fun generateString(roundingStrategy: RoundingStrategy, unit: String?, separator: String): String

    object Empty : Data() {

      override fun generateString(roundingStrategy: RoundingStrategy, unit: String?, separator: String): String {
        return "no data${separator}(no iterations data)"
      }
    }

    data class Success(val average: Double, val iterations: Long) : Data() {

      override fun generateString(roundingStrategy: RoundingStrategy, unit: String?, separator: String): String {
        val postfix = when (unit) {
          null -> ""

          else -> " ${unit.trim()}"
        }

        return "${roundingStrategy.round(average)}$postfix${separator}($iterations iterations)"
      }
    }
  }

  companion object {

    fun createForDouble(name: String, roundingStrategy: RoundingStrategy, unit: String? = null): Average<Double> =
      Average(DoubleValue(), unit, name, roundingStrategy)

    fun createForLong(name: String, roundingStrategy: RoundingStrategy, unit: String? = null): Average<Long> =
      Average(LongValue(), unit, name, roundingStrategy)
  }
}
