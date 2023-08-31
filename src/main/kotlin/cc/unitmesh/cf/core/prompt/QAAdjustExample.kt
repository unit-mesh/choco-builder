package cc.unitmesh.cf.core.prompt

import kotlinx.serialization.Serializable

@Serializable
data class QAAdjustExample(
    val input: String,
    val output: String,
    val action: String = "",
    val answer: String = "",
) {
}