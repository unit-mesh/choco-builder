package cc.unitmesh.cf.core.prompt

import kotlinx.serialization.Serializable

@Serializable
data class QAUpdateExample(
    override val question: String,
    override val answer: String,
    val nextAction: String = "",
    val finalOutput: String = "",
) : PromptExample {
}