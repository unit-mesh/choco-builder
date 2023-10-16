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
        // format; repoUrl: to owner/repo, like https://github.com/unitmesh/devti will be unitmesh/devti
        var repoUrl = repoUrl.split("/").takeLast(2).joinToString("/")

        // if repoUrl ends with .git, remove it
        repoUrl = if (repoUrl.endsWith(".git")) repoUrl.substring(0, repoUrl.length - 4) else repoUrl

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
}