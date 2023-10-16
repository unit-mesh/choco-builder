package cc.unitmesh.genius.project

import cc.unitmesh.genius.devops.Issue
import cc.unitmesh.genius.devops.KanbanFactory
import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.Serializable

@Serializable
data class GeniusProject(
    val name: String = "",
    val path: String = "",
    val store: GeniusStore = GeniusStore(),
    val kanban: GeniusKanban = GeniusKanban(),
) {
    var repoUrl: String = ""

    fun fetchStory(id: String): Issue {
        return KanbanFactory.fromRepositoryUrl(repoUrl, kanban.token)!!.fetch(id)
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