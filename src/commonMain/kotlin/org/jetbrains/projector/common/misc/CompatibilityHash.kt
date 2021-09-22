package org.jetbrains.projector.common.misc

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.descriptors.elementNames

private val List<Int>.compatibilityHash: Int get() = reduce { acc, c -> 31 * acc + c }

@Suppress("DEPRECATION")  // https://youtrack.jetbrains.com/issue/KT-47644
private val String.compatibilityHash: Int
  get() = map(Char::toInt).compatibilityHash

private val Boolean.compatibilityHash: Int get() = if (this) 1 else 0

@OptIn(ExperimentalSerializationApi::class)
val SerialDescriptor.compatibilityHash: Int
  get() {
    val subDescriptorsHashes = when (kind) {
      is SerialKind.ENUM -> emptyList()

      else -> elementDescriptors.map(SerialDescriptor::compatibilityHash)
    }

    return listOf(
      serialName.compatibilityHash,
      kind.toString().compatibilityHash,
      isNullable.compatibilityHash,
      elementsCount,
      *elementNames.map(String::compatibilityHash).toTypedArray(),
      *subDescriptorsHashes.toTypedArray()
    )
      .compatibilityHash
  }
