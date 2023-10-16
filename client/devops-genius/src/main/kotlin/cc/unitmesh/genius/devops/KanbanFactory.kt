package cc.unitmesh.genius.devops

import cc.unitmesh.genius.devops.kanban.GitHubKanban
import cc.unitmesh.genius.devops.kanban.GitlabKanban
import cc.unitmesh.genius.devops.kanban.Kanban
import java.net.URL

object KanbanFactory {

    /**
     * Creates a Kanban object based on the provided repository URL.
     *
     * @param url the URL of the repository
     * @return a Kanban object representing the repository's Kanban board, or null if the URL is invalid or unsupported
     */
    fun fromRepositoryUrl(url: String, token: String): Kanban? {
        return try {
            val parsedUrl = URL(url)
            val host = parsedUrl.host

            when {
                host.contains("github.com") -> GitHubKanban(url, token)
                host.contains("gitlab.com") -> GitlabKanban(url, token)
                else -> GitlabKanban(url, token)
            }
        } catch (e: Exception) {
            null
        }
    }
}