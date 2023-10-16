package cc.unitmesh.genius.devops.kanban

import cc.unitmesh.genius.devops.Issue
import org.gitlab4j.api.GitLabApi

class GitlabKanban(val repoUrl: String, val token: String) : Kanban {
    private var gitLabApi: GitLabApi = GitLabApi(repoUrl, token)
    override fun fetch(id: String): Issue {
        val issue: org.gitlab4j.api.models.Issue = gitLabApi.issuesApi.getIssue(repoUrl, id.toInt())
        return Issue(
            issue.iid.toString(),
            issue.title,
            issue.description,
            issue.webUrl,
            issue.labels,
            issue.assignees.map { it.name }
        )
    }

}