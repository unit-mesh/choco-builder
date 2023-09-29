package cc.unitmesh.prompt.config

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Connection(
    val file: String = "connections.yml",
    val vars: List<Variable> = listOf(),
)

@Serializable
sealed class Variable {
    @SerialName("key-value")
    @Serializable
    data class KeyValue(val key: String, val value: String) : Variable()

    @SerialName("range")
    @Serializable
    data class Range(val key: String, val range: String, val step: String) : Variable()
}

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

@Serializable
data class Job(
    val description: String,
    val template: String,
    val connection: Connection,
    val vars: Map<String, String>,
    val validate: List<ValidateItem>?,
)

@Serializable
data class Configuration(
    val name: String,
    val description: String,
    val jobs: Map<String, Job>,
) {
    companion object {

        fun fromString(yamlString: String): Configuration? {
            return try {
                val configuration = YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property)
                Yaml(
                    configuration = configuration,
                ).decodeFromString(serializer(), yamlString)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}

