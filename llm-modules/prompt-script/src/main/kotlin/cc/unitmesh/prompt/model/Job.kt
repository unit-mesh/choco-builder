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
     * Connection is a file that will be serialized to [cc.unitmesh.connection.BaseConnection] class
     */
    val connection: String = "connections.yml",
    val vars: Map<String, String> = mapOf(),
    val strategy: List<StrategyItem> = listOf(),
    val validate: List<ValidateItem> = listOf(),
) {
    fun buildValidators(input: String): List<Validator> {
        val validators: List<Validator> = validate.map {
            when (it) {
                is ValidateItem.ExtToolItem -> {
                    ExtToolValidator(it.value, input)
                }

                is ValidateItem.JsonItem -> {
                    JsonValidator(input)
                }

                is ValidateItem.JsonPathItem -> {
                    JsonPathValidator(it.value, input)
                }

                is ValidateItem.MarkdownCodeBlockItem -> {
                    MarkdownCodeBlockValidator(input)
                }

                is ValidateItem.RegexItem -> {
                    RegexValidator(it.value, input)
                }

                is ValidateItem.StringRuleItem -> {
                    StringValidator(it.value, input)
                }
            }
        }


        return validators
    }
}
