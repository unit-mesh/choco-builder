package cc.unitmesh.cf.core.prompt

import kotlinx.serialization.Serializable

@Serializable
data class PromptExample(
    val question: String,
    val answer: String,
)