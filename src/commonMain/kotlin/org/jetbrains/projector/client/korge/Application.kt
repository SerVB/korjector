package org.jetbrains.projector.client.korge

import com.soywiz.korge.view.Stage
import org.jetbrains.projector.client.common.misc.ParamsProvider
import org.jetbrains.projector.client.korge.state.ClientAction
import org.jetbrains.projector.client.korge.state.ClientStateMachine
import kotlin.random.Random

class Application(private val stage: Stage) {

    private val stateMachine = ClientStateMachine(stage)
    private val windowSizeController = WindowSizeController(stage, stateMachine)

    fun start() {
        val scheme = when (ParamsProvider.ENABLE_WSS) {
            false -> "ws://"
            true -> "wss://"
        }
        val host = ParamsProvider.HOST
        val port = ParamsProvider.PORT
        val path = ParamsProvider.PATH
        val relayPath = when (ParamsProvider.ENABLE_RELAY) {
            true -> "/connect/${ParamsProvider.RELAY_SERVER_ID}/${generateKey()}"
            false -> ""
        }

        val url = "$scheme$host:$port$path$relayPath"

        stateMachine.fire(
            ClientAction.Start(
                stage = stage,
                stateMachine = stateMachine,
                url = url,
                windowSizeController = windowSizeController
            )
        )

        stateMachine.runMainLoop()
    }

    companion object {

        private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        private fun generateKey(): String =
            (1..20)
                .map { Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")
    }
}
