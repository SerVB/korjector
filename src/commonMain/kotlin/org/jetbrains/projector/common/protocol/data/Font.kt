package org.jetbrains.projector.common.protocol.data

import kotlinx.serialization.Serializable

@Serializable
data class FontDataHolder(
    val fontId: Short,
    val fontData: TtfFontData,  // todo: provide other variants like type1font and unserializableFont(name, style)
)

@Serializable
data class TtfFontData(val ttfBase64: String)
