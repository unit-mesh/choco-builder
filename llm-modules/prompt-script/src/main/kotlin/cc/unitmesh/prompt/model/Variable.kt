package cc.unitmesh.prompt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Variable {
    @SerialName("key-value")
    @Serializable
    data class KeyValue(val key: String, val value: String) : Variable()

    @SerialName("range")
    @Serializable
    data class Range(val key: String, val range: String, val step: String) : Variable()
}