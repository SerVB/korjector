package org.jetbrains.projector.client.common.misc

object ParamsProvider {

  val SYSTEM_SCALING_RATIO
    get() = 1.0  // get every time because it can be changed  // todo: switch from 1.0 to smth reasonable
  val USER_SCALING_RATIO: Double = 1.0

  val CLIPPING_BORDERS: Boolean = false
  val HOST: String = "localhost"
  val PORT: String = "8887"
  val PATH: String = "/"
  val RELAY_SERVER_ID: String? = null
  val ENABLE_RELAY: Boolean get() = RELAY_SERVER_ID != null
  val LOG_UNSUPPORTED_EVENTS: Boolean = true
  val DOUBLE_BUFFERING: Boolean = false
  val ENABLE_COMPRESSION: Boolean = false
  val IMAGE_TTL: Double = 60_000.0
  val FLUSH_DELAY: Int? = 1
  val SHOW_TEXT_WIDTH: Boolean = false
  val SHOW_SENT_RECEIVED: Boolean = false
  val SHOW_PING: Boolean = false
  val BACKGROUND_COLOR: String = "#282"
  val PING_AVERAGE_COUNT: Int? = null
  val PING_INTERVAL: Int = 1000
  val SHOW_PROCESSING_TIME: Boolean = false
  val REPAINT_AREA: RepaintAreaSetting = RepaintAreaSetting.Disabled
  val SPECULATIVE_TYPING: Boolean = false
  val ENABLE_WSS: Boolean = false
  val HANDSHAKE_TOKEN: String? = null
  val IDE_WINDOW_ID: Int? = null
  val SHOW_NOT_SECURE_WARNING: Boolean = false
  val REPAINT_INTERVAL_MS: Int = 333
  val IMAGE_CACHE_SIZE_CHARS: Int = 5_000_000
  val BLOCK_CLOSING: Boolean = true
  val SCALING_RATIO: Double
    get() = SYSTEM_SCALING_RATIO * USER_SCALING_RATIO
}

sealed class RepaintAreaSetting {

  object Disabled : RepaintAreaSetting()

  data class Enabled(var show: Boolean) : RepaintAreaSetting()
}
