package cc.unitmesh.prompt.model

import kotlinx.serialization.Serializable

@Serializable
data class Connection(
    val file: String = "connections.yml",
    val vars: List<Variable> = listOf(),
)