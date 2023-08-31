package cc.unitmesh.cf.core.prompt

import kotlinx.serialization.Serializable

@Serializable
data class CoTExample(
    val question: String,
    val answer: String,
)