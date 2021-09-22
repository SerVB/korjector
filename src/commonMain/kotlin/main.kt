import com.soywiz.klogger.Logger
import com.soywiz.korge.Korge
import com.soywiz.korge.view.centerOn
import com.soywiz.korge.view.text
import com.soywiz.korim.color.Colors

suspend fun main() = Korge(
    width = 800, height = 600,
    title = "korjector",
) {
    Logger.defaultLevel = Logger.Level.INFO

    text("TODO", color = Colors.MAGENTA).centerOn(this)
}
