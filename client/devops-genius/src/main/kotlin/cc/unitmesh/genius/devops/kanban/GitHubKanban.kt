package cc.unitmesh.genius.devops.kanban

import cc.unitmesh.genius.devops.Issue
import org.kohsuke.github.GitHub
import org.kohsuke.github.GitHubBuilder

class GitHubKanban(private val repoUrl: String, private val token: String) : Kanban {
    private val gitHub: GitHub

    init {
        try {
            gitHub = GitHubBuilder()
                .withOAuthToken(token)
                .build()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun fetch(id: String): Issue {
        val repoUrl = formatUrl(this.repoUrl)
        val issue = gitHub.getRepository(repoUrl).getIssue(Integer.parseInt(id))

        return Issue(
            issue.number.toString(),
            issue.title,
            issue.body,
            issue.url.toString(),
            issue.labels.map { it.name },
            issue.assignees.map { it.name }
        )
    }

    companion object {
        /**
         * Formats the repository URL to owner/repo format.
         *
         * The formatUrl method takes the repository URL and formats it to the owner/repo format.
         * For example, if the repository URL is "https://github.com/unitmesh/devti",
         * the formatted URL will be "unitmesh/devti".
         *
         * @return The formatted repository URL in the owner/repo format.
         */
        fun formatUrl(repoUrl: String): String {
            var url = repoUrl.split("/").takeLast(2).joinToString("/")
            url = if (url.endsWith(".git")) url.substring(0, url.length - 4) else url
            return url
        }
    }
}