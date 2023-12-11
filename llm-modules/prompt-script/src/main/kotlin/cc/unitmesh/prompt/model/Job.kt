package cc.unitmesh.prompt.model

import cc.unitmesh.prompt.validate.*
import com.google.gson.JsonElement
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
    fun buildValidators(llmResult: String, dataItem: JsonElement): List<Validator> {
        return validate.map {
            when (it) {
                is ValidateRule.ExtTool -> ExtToolValidator(it.value, llmResult, it.options)
                is ValidateRule.Json -> JsonValidator(llmResult)
                is ValidateRule.JsonPath -> JsonPathValidator(it.value, llmResult)
                is ValidateRule.MarkdownCodeBlock -> MarkdownCodeBlockValidator(llmResult)
                is ValidateRule.Regex -> RegexValidator(it.value, llmResult)
                is ValidateRule.StringRule -> StringValidator(it.value, llmResult)
                is ValidateRule.CodeCompletion -> CodeCompletionValidator(llmResult, it.selection, dataItem, it.language)
            }
        }
    }
}
