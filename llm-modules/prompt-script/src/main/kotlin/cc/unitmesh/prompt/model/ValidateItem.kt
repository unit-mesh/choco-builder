package cc.unitmesh.prompt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ValidateItem {
    @SerialName("json-path")
    @Serializable
    data class JsonPathItem(val value: String) : ValidateItem()

    @SerialName("string")
    @Serializable
    data class StringItem(val value: String) : ValidateItem()

    @SerialName("regex")
    @Serializable
    data class RegexItem(val value: String) : ValidateItem()

    @SerialName("markdown-code")
    @Serializable
    data class MarkdownCodeBlockItem(val value: String) : ValidateItem()

    @SerialName("json")
    @Serializable
    data class JsonItem(val value: String) : ValidateItem()

    @SerialName("ext-tool")
    @Serializable
    data class ExtToolItem(val value: String) : ValidateItem()
}