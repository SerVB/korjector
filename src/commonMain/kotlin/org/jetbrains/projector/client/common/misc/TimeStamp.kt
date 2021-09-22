package org.jetbrains.projector.client.common.misc

import com.soywiz.klock.DateTime

object TimeStamp {
  val current: Double = DateTime.nowUnix()
}
