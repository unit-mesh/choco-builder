package cc.unitmesh.genius.project

import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.core.llms.MockLlmProvider
import cc.unitmesh.connection.ConnectionConfig
import cc.unitmesh.connection.MockLlmConnection
import cc.unitmesh.connection.OpenAiConnection
import cc.unitmesh.genius.devops.Issue
import cc.unitmesh.genius.devops.KanbanFactory
import cc.unitmesh.openai.OpenAiProvider
import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Path

@Serializable
data class GeniusProject(
    val name: String = "",
    val path: String = "",
    val store: GeniusStore? = null,
    val kanban: GeniusKanban? = null,
    val commitLog: GeniusCommitLog? = null,
    val connection: String = "connection.yml",
) {
    var repoUrl: String = ""

    fun fetchStory(id: String): Issue {
        return KanbanFactory.fromRepositoryUrl(repoUrl, kanban!!.token)!!.fetch(id)
    }

    fun connector(): LlmProvider {
        val text = File(connection).readBytes().toString(Charsets.UTF_8)
        val configuration = YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property)
        val connection = Yaml(configuration = configuration).decodeFromString<ConnectionConfig>(text).convert()

        val llmProvider = when (connection) {
            is OpenAiConnection -> OpenAiProvider(connection.apiKey, connection.apiHost)
            is MockLlmConnection -> MockLlmProvider(connection.response)
            else -> throw Exception("unsupported connection type: ${connection.type}")
        }

        return llmProvider
    }


    companion object {
        fun fromYml(yaml: String): GeniusProject {
            val conf = YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property)
            return Yaml(configuration = conf).decodeFromString(serializer(), yaml)
        }
    }
}

/**
 * Vector store of a project will be used to search project code and documents.
 */
@Serializable
data class GeniusStore(
    val indexName: String = "",
)

/**
 * Kanban configuration of project, will be used to fetch issues from kanban board.
 */
@Serializable
data class GeniusKanban(
    val url: String = "",
    val token: String = "",
    val type: String = "",
)

@Serializable
data class GeniusCommitLog(
    val ignoreType: List<String>,
    /**
     * Ignore files when generate commit log, which is a list of glob pattern.
     */
    val ignorePatterns: List<String>,
) {
    @Transient
    private val compiledPatterns = ignorePatterns.map {
        FileSystems.getDefault().getPathMatcher("glob:$it")
    }

    fun isIgnoreType(type: String): Boolean {
        return !ignoreType.contains(type)
    }

    fun isIgnoreFile(filename: String): Boolean {
        return compiledPatterns.none { it.matches(Path.of(filename)) }
    }
}
