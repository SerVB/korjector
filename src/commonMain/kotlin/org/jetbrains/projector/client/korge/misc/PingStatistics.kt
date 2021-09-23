package org.jetbrains.projector.client.korge.misc

import com.soywiz.klogger.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mainStage
import org.jetbrains.projector.client.common.misc.ParamsProvider
import org.jetbrains.projector.client.common.misc.TimeStamp
import org.jetbrains.projector.common.protocol.toClient.ServerPingReplyEvent

class PingStatistics(
  private val openingTimeStamp: Int,
  private val requestPing: () -> Unit,
) {

//  private val pingShower = when (ParamsProvider.SHOW_PING) {
//    true -> DivPingShower()
//    false -> NoPingShower
//  }  // todo

  private var job: Job? = null

  fun onHandshakeFinished() {
    job = mainStage.launch {
      while (true) {
        delay(ParamsProvider.PING_INTERVAL.toLong())
        requestPing()
      }
    }

//    pingShower.onHandshakeFinished()  // todo
  }

  fun onPingReply(pingReply: ServerPingReplyEvent) {
    val currentTimeStamp = TimeStamp.current.toInt() - openingTimeStamp

    val ping = currentTimeStamp - pingReply.clientTimeStamp

    ClientStats.simplePingAverage.add(ping.toDouble())

    val clientToServer = pingReply.serverReadEventTimeStamp - pingReply.clientTimeStamp
    val serverToClient = currentTimeStamp - pingReply.serverReadEventTimeStamp

    logger.debug { "Ping: ${clientToServer + serverToClient} ms ($clientToServer ms + $serverToClient ms)" }

//    pingShower.onPingReply(clientToServer, serverToClient, ping)  // todo
  }

  fun onClose() {
    job?.cancel()
    job = null

//    pingShower.onClose()  // todo
  }

  private companion object {

    private val logger = Logger<PingStatistics>()
  }
}
