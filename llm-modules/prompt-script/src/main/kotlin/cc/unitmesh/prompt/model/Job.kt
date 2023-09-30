package cc.unitmesh.prompt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Job(
    val description: String,
    val template: String,
    @SerialName("template-datasource")
    /**
     * Currently only support file datasource, and just one file.
     */
    val templateDatasource: List<TemplateDatasource> = listOf(),
    /**
     * Connection is a file that will be serialized to [cc.unitmesh.connection.BaseConnection] class
     */
    val connection: Connection,
    val vars: Map<String, String>,
    val validate: List<ValidateItem>?,
)

@Serializable
sealed class TemplateDatasource {
    @Serializable
    @SerialName("file")
    data class File(val value: String) : TemplateDatasource()
}