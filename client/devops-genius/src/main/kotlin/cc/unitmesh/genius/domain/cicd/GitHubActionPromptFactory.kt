package cc.unitmesh.genius.domain.cicd

import cc.unitmesh.genius.project.SimpleProject
import cc.unitmesh.genius.prompt.PromptFactory

class GitHubActionPromptFactory : PromptFactory("prompts/cicd/github-action") {
    override fun createPrompt(project: SimpleProject, description: String): String {
        TODO("Not yet implemented")
    }
}