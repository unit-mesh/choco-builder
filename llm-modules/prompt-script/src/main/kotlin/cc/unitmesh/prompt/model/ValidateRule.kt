package cc.unitmesh.prompt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ValidateRule will be used to validate the job's result.
 * Currently, we support JsonPath, String, Regex, MarkdownCodeBlock, Json, ExtTool.
 * For example:
 *
 * ```yaml
 * validate:
 *   - type: json-path
 *     value: $.output.id
 *   - type: string
 *     value: output.length < 100
 * ```
 *
 */
@Serializable
sealed class ValidateRule {
    /**
     * type: json-path, is a json path expression, which will be used to extract the value from the job's result.
     */
    @SerialName("json-path")
    @Serializable
    data class JsonPath(val value: String) : ValidateRule()

    /**
     * type: string, will use string expression, like the [String.contains] method to check the job's result.
     */
    @SerialName("string")
    @Serializable
    data class StringRule(val value: String) : ValidateRule()

    /**
     * type: regex, will use regex expression to check the job's result.
     */
    @SerialName("regex")
    @Serializable
    data class Regex(val value: String) : ValidateRule()

    /**
     * type: markdown-code, will verify the job's result is a valid markdown code block.
     */
    @SerialName("markdown-code")
    @Serializable
    data class MarkdownCodeBlock(val value: String) : ValidateRule()

    /**
     * type: json, will check the job's result is a valid json.
     */
    @SerialName("json")
    @Serializable
    data class Json(val value: String) : ValidateRule()

    /**
     * type: ext-tool, will use the external tool to check the job's result, like PlantUML, Graphviz, etc.
     */
    @SerialName("ext-tool")
    @Serializable
    data class ExtTool(val value: String) : ValidateRule()
}