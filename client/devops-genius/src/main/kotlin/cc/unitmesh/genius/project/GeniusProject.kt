package cc.unitmesh.genius.project

import cc.unitmesh.genius.devops.Issue
import cc.unitmesh.genius.devops.KanbanFactory
import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.nio.file.FileSystems
import java.nio.file.Path

@Serializable
data class GeniusProject(
    val name: String = "",
    val path: String = "",
    val store: GeniusStore? = null,
    val kanban: GeniusKanban? = null,
    val commitLog: GeniusCommitLog? = null,
) {
    var repoUrl: String = ""

    fun fetchStory(id: String): Issue {
        return KanbanFactory.fromRepositoryUrl(repoUrl, kanban!!.token)!!.fetch(id)
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
