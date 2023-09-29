package cc.unitmesh.prompt.model

import kotlinx.serialization.Serializable

@Serializable
data class Job(
    val description: String,
    val template: String,
    /**
     * Data is a file that will be serialized to class
     */
    val data: String? = null,
    /**
     * Connection is a file that will be serialized to [cc.unitmesh.connection.BaseConnection] class
     */
    val connection: Connection,
    val vars: Map<String, String>,
    val validate: List<ValidateItem>?,
)