package org.jetbrains.projector.common.protocol.toClient

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Serializable
data class MainWindow(
  val title: String?,
  val pngBase64Icon: String?,
)

private val json = Json {}
private val serializer = ListSerializer(MainWindow.serializer())

fun List<MainWindow>.toJson(): String = json.encodeToString(serializer, this)

fun String.toMainWindowList(): List<MainWindow> = json.decodeFromString(serializer, this)
