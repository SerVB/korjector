package org.jetbrains.projector.common.statistics

import org.jetbrains.projector.common.misc.toString
import kotlin.math.absoluteValue
import kotlin.math.roundToLong

sealed class RoundingStrategy {

  abstract fun round(value: Double): String


  class FractionDigits(private val fractionDigitsToLeave: Int) : RoundingStrategy() {

    override fun round(value: Double): String = value.toString(fractionDigitsToLeave)
  }

  object Multiplier : RoundingStrategy() {

    override fun round(value: Double): String {
      // todo: support more coefficients and provide logic for fractional numbers
      val digitCount = value.absoluteValue.roundToLong().toString().length

      val int = value.roundToLong()

      val (coefficient, degree) = coefficientToDegreeAsc
        .lastOrNull { (_, degree) -> degree < digitCount }
        ?: noCoefficientToDegree

      return "${int.toString().dropLast(degree)}$coefficient"
    }

    private val noCoefficientToDegree = "" to 0

    private val coefficientToDegreeAsc = listOf(
      "K" to 3,
      "M" to 6
    )
  }
}
