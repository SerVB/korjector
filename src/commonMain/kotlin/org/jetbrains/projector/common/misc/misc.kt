package org.jetbrains.projector.common.misc

import com.soywiz.korio.lang.format

object Do { // https://youtrack.jetbrains.com/issue/KT-12380

  inline infix fun <reified T> exhaustive(any: T) = any
}

// https://youtrack.jetbrains.com/issue/KT-9374
fun Double.toString(fractionDigitsToLeave: Int): String = "%.${fractionDigitsToLeave}f".format(this)
