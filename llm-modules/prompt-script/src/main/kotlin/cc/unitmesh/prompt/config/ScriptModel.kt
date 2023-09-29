package cc.unitmesh.prompt.config

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Connection(
    val type: String,
    @Contextual
    val vars: Map<String, String>,
)
//
//@Serializable
//sealed class Variable {
//    @Serializable
//    class ModelVariable(val value: String) : Variable()
//
//    @Serializable
//    data class RangeVariable(val type: String, val range: String, val step: String) : Variable()
//
//    @Serializable
//    data class FloatRangeVariable(val type: String, val range: ClosedRange<Float>, val step: Float) : Variable()
//}

@Serializable
data class ValidateItem(
    val type: String,
    val value: String,
)

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
                Yaml.default.decodeFromString(serializer(), yamlString)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}

