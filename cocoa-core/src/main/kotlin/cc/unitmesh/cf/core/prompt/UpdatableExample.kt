package cc.unitmesh.cf.core.prompt

import kotlinx.serialization.Serializable

@Serializable
data class UpdatableExample(
    override val question: String,
    override val answer: String,
    val userResponse: String = "",
    val finalOutput: String = "",
) : PromptExample {
}