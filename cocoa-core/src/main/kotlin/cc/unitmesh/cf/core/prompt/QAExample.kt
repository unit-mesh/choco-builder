package cc.unitmesh.cf.core.prompt

import kotlinx.serialization.Serializable

@Serializable
open class QAExample(
    override val question: String,
    override val answer: String,
) : PromptExample {

}