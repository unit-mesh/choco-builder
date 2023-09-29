package cc.unitmesh.prompt.model

import kotlinx.serialization.Serializable

@Serializable
data class Job(
    val description: String,
    val template: String,
    val connection: Connection,
    val vars: Map<String, String>,
    val validate: List<ValidateItem>?,
)