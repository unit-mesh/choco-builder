package cc.unitmesh.prompt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ValidateItem will be used to validate the job's result.
 */
@Serializable
sealed class ValidateItem {
    /**
     * JsonPath is a json path expression, which will be used to extract the value from the job's result.
     */
    @SerialName("json-path")
    @Serializable
    data class JsonPathItem(val value: String) : ValidateItem()

    /**
     * String will use string expression, like the [String.contains] method to check the job's result.
     */
    @SerialName("string")
    @Serializable
    data class StringRuleItem(val value: String) : ValidateItem()

    /**
     * Regex will use regex expression to check the job's result.
     */
    @SerialName("regex")
    @Serializable
    data class RegexItem(val value: String) : ValidateItem()

    /**
     * MarkdownCodeBlock will verify the job's result is a valid markdown code block.
     */
    @SerialName("markdown-code")
    @Serializable
    data class MarkdownCodeBlockItem(val value: String) : ValidateItem()

    /**
     * Json will check the job's result is a valid json.
     */
    @SerialName("json")
    @Serializable
    data class JsonItem(val value: String) : ValidateItem()

    /**
     * ExtTool will use the external tool to check the job's result, like PlantUML, Graphviz, etc.
     */
    @SerialName("ext-tool")
    @Serializable
    data class ExtToolItem(val value: String) : ValidateItem()
}