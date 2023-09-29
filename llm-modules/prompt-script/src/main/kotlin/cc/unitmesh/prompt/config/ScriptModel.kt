package cc.unitmesh.prompt.config

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.Serializable

@Serializable
data class PromptScript(
    val name: String,
    val description: String,
    val jobs: Map<String, Job>,
) {
    companion object {
        fun fromString(yamlString: String): PromptScript? {
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

