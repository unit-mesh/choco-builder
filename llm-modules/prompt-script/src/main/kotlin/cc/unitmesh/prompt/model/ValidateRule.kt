package cc.unitmesh.prompt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ValidateRule will be used to validate the job's result.
 */
@Serializable
sealed class ValidateRule {
    /**
     * JsonPath is a json path expression, which will be used to extract the value from the job's result.
     */
    @SerialName("json-path")
    @Serializable
    data class JsonPath(val value: String) : ValidateRule()

    /**
     * String will use string expression, like the [String.contains] method to check the job's result.
     */
    @SerialName("string")
    @Serializable
    data class StringRule(val value: String) : ValidateRule()

    /**
     * Regex will use regex expression to check the job's result.
     */
    @SerialName("regex")
    @Serializable
    data class Regex(val value: String) : ValidateRule()

    /**
     * MarkdownCodeBlock will verify the job's result is a valid markdown code block.
     */
    @SerialName("markdown-code")
    @Serializable
    data class MarkdownCodeBlock(val value: String) : ValidateRule()

    /**
     * Json will check the job's result is a valid json.
     */
    @SerialName("json")
    @Serializable
    data class Json(val value: String) : ValidateRule()

    /**
     * ExtTool will use the external tool to check the job's result, like PlantUML, Graphviz, etc.
     */
    @SerialName("ext-tool")
    @Serializable
    data class ExtTool(val value: String) : ValidateRule()
}