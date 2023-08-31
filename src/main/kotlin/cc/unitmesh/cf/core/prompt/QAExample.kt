package cc.unitmesh.cf.core.prompt

import kotlinx.serialization.Serializable

@Serializable
open class QAExample(
    open val question: String,
    open val answer: String,
)