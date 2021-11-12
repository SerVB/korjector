import com.soywiz.klogger.Logger
import com.soywiz.korge.Korge
import com.soywiz.korge.baseview.BaseView
import com.soywiz.korge.component.ResizeComponent
import com.soywiz.korge.input.onClick
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.ui.uiCheckBox
import com.soywiz.korge.ui.uiTextInput
import com.soywiz.korge.ui.uiVerticalStack
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.Views
import com.soywiz.korge.view.centerOnStage
import com.soywiz.korge.view.solidRect
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.*
import com.soywiz.korma.geom.Anchor
import com.soywiz.korma.geom.ScaleMode
import org.jetbrains.projector.client.common.misc.ParamsProvider
import org.jetbrains.projector.client.korge.Application
import kotlin.native.concurrent.*

@ThreadLocal
lateinit var mainStage: Stage
    private set

suspend fun main() = Korge(
    width = 800, height = 600,
    title = "korjector",
) {
    RegisteredImageFormats.register(PNG)

    Logger.defaultLevel = Logger.Level.DEBUG
    mainStage = stage

    val application = Application()
    showSetupScreen(application)
}

private fun Stage.showSetupScreen(application: Application) {
    views.virtualWidthDouble = gameWindow.width.toDouble()
    views.virtualHeightDouble = gameWindow.height.toDouble()
    views.clipBorders = false
    views.scaleAnchor = Anchor.TOP_LEFT
    views.scaleMode = ScaleMode.NO_SCALE
    val rect = solidRect(gameWindow.width, gameWindow.height, RGBA(34, 136, 34))

    val maxLayoutWidth = 600.0
    val layoutPadding = 5.0

    val mainLayout = uiVerticalStack(width = maxLayoutWidth, padding = layoutPadding) {

        val urlInput = uiTextInput("ws://localhost:8887") {
            onTextUpdated.add {
                textColor = Colors.BLACK
            }
        }

        uiCheckBox(text = "Fullscreen") {
            onChange.add {
                gameWindow.fullscreen = it.checked
            }
        }

        uiButton(text = "Connect") {
            onClick {
                if (ParamsProvider.loadParamsFromUrl(urlInput.text)) {
                    this@uiVerticalStack.removeFromParent()
                    application.start()
                } else {
                    urlInput.textColor = Colors.RED
                }
            }
        }
    }.centerOnStage()

    addComponent(object : ResizeComponent {
        override val view: BaseView
            get() = stage

        override fun resized(views: Views, width: Int, height: Int) {
            rect.scaledHeight = gameWindow.height.toDouble()
            rect.scaledWidth = gameWindow.width.toDouble()

            stage.views.virtualWidthDouble = gameWindow.width.toDouble()
            stage.views.virtualHeightDouble = gameWindow.height.toDouble()

            mainLayout.scaledWidth = (gameWindow.width.toDouble() - 20).coerceAtMost(maxLayoutWidth)

            mainLayout.centerOnStage()
        }

    })
}
