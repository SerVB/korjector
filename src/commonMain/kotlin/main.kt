import com.soywiz.klogger.Logger
import com.soywiz.korge.Korge
import org.jetbrains.projector.client.korge.Application

suspend fun main() = Korge(
    width = 800, height = 600,
    title = "korjector",
) {
    Logger.defaultLevel = Logger.Level.INFO

    Application(stage).apply {
        start()
    }
}
