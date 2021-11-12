package org.jetbrains.projector.client.korge.misc

import com.soywiz.klogger.Logger
import org.jetbrains.projector.client.common.misc.TimeStamp
import org.jetbrains.projector.common.statistics.Average
import org.jetbrains.projector.common.statistics.Rate
import org.jetbrains.projector.common.statistics.RoundingStrategy
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object ClientStats {

  private val logger = Logger<ClientStats>()

  val simplePingAverage = Average.createForDouble("simple ping", RoundingStrategy.FractionDigits(2))

  val toClientMessageSizeAverage = Average.createForLong("to-client message size", RoundingStrategy.Multiplier, "bytes")
  val toClientMessageSizeRate = Rate.createForLong("to-client message size", RoundingStrategy.Multiplier, "bytes")

  val toClientCompressionRatioAverage =
    Average.createForDouble("to-client compression ratio", RoundingStrategy.FractionDigits(2))

  val drawEventCountAverage = Average.createForLong("draw event count", RoundingStrategy.Multiplier)
  val drawEventCountRate = Rate.createForLong("draw event count", RoundingStrategy.Multiplier)

  val decompressingTimeMsAverage =
    Average.createForDouble("decompressing time", RoundingStrategy.FractionDigits(2), "ms")
  val decompressingTimeMsRate = Rate.createForDouble("decompressing time", RoundingStrategy.FractionDigits(2), "ms")

  val decodingTimeMsAverage = Average.createForDouble("decoding time", RoundingStrategy.FractionDigits(2), "ms")
  val decodingTimeMsRate = Rate.createForDouble("decoding time", RoundingStrategy.FractionDigits(2), "ms")

  val drawingTimeMsAverage = Average.createForDouble("drawing time", RoundingStrategy.FractionDigits(2), "ms")
  val drawingTimeMsRate = Rate.createForDouble("drawing time", RoundingStrategy.FractionDigits(2), "ms")

  val otherProcessingTimeMsAverage =
    Average.createForDouble("other processing time", RoundingStrategy.FractionDigits(2), "ms")
  val otherProcessingTimeMsRate =
    Rate.createForDouble("other processing time", RoundingStrategy.FractionDigits(2), "ms")

  // it's sum of the previous stats just for convenience
  val totalTimeMsAverage = Average.createForDouble("total (sum) time", RoundingStrategy.FractionDigits(2), "ms")
  val totalTimeMsRate = Rate.createForDouble("total (sum) time", RoundingStrategy.FractionDigits(2), "ms")

  private val averageList = listOf(
    simplePingAverage,
    toClientMessageSizeAverage,
    toClientCompressionRatioAverage,
    drawEventCountAverage,
    decompressingTimeMsAverage,
    decodingTimeMsAverage,
    drawingTimeMsAverage,
    otherProcessingTimeMsAverage,
    totalTimeMsAverage
  )

  private val rateList = listOf(
    toClientMessageSizeRate,
    drawEventCountRate,
    decompressingTimeMsRate,
    decodingTimeMsRate,
    drawingTimeMsRate,
    otherProcessingTimeMsRate,
    totalTimeMsRate
  )

  fun printStats() {
    logger.info {
      val averageStats = averageList
        .map(Average<*>::reset)
        .joinToString("\n") { it.generateString(SEPARATOR) }

      val rateStats = rateList
        .map { it.reset(TimeStamp.current) }
        .joinToString("\n") { it.generateString(SEPARATOR) }

      """
        |Stats:
        |
        |$averageStats
        |
        |$rateStats
        |
        |Stats are reset!
      """.trimMargin()
    }
  }

  fun resetStats(timeStamp: Double) {
    averageList.forEach { it.reset() }
    rateList.forEach { it.reset(timeStamp) }
  }

  private const val SEPARATOR = "\t"
}
