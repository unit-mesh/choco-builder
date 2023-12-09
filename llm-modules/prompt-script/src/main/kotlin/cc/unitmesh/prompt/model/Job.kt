package cc.unitmesh.prompt.model

import cc.unitmesh.prompt.validate.*
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
     * Connection is a file that will be serialized to [cc.unitmesh.connection.ConnectionConfig] class
     */
    val connection: String = "connections.yml",
    val vars: Map<String, String> = mapOf(),
    val strategy: List<JobStrategy> = listOf(),
    val validate: List<ValidateRule> = listOf(),
    @SerialName("log-path")
    val logPath: String = "logs",
) {
    fun buildValidators(input: String): List<Validator> {
        return validate.map {
            when (it) {
                is ValidateRule.ExtTool -> ExtToolValidator(it.value, input, it.options)
                is ValidateRule.Json -> JsonValidator(input)
                is ValidateRule.JsonPath -> JsonPathValidator(it.value, input)
                is ValidateRule.MarkdownCodeBlock -> MarkdownCodeBlockValidator(input)
                is ValidateRule.Regex -> RegexValidator(it.value, input)
                is ValidateRule.StringRule -> StringValidator(it.value, input)
            }
        }
    }
}
