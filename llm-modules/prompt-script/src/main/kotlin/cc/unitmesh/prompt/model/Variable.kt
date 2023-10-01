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
    data class Range(val key: String, val range: String, val step: String) : Variable() {
        /**
         * A range string, could be:
         * 1. use `~` as format, e.g.: 0.0~1.0, 0~100, 0~1000, 0.0~100.0, 0.0~1000.0
         * 2. use `..` as format, e.g.: 0.0..1.0, 0..100, 0..1000, 0.0..100.0, 0.0..1000.0
         * 3. use `:` as format, e.g.: 0.0:1.0, 0:100, 0:1000, 0.0:100.0, 0.0:1000.0
         * 4. use `to` as format, e.g.: 0.0 to 1.0, 0 to 100, 0 to 1000, 0.0 to 100.0, 0.0 to 1000.0
         *
         * So, we will parse [step] to confirm is int or float, then parse [range] to get [ClosedRange].
         *
         */
        fun toRange(): ClosedRange<Double> {
            val separator = when {
                range.contains("~") -> "~"
                range.contains("..") -> ".."
                range.contains(":") -> ":"
                range.contains(" to ") -> " to "
                else -> throw IllegalArgumentException("Unsupported range format: $range")
            }

            val (start: Double, end: Double) = range.split(separator).map { it.trim().toDouble() }
            return start.rangeTo(end)
        }
    }
}