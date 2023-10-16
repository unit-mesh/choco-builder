package cc.unitmesh.genius.domain.cicd

import cc.unitmesh.genius.project.GeniusProject
import cc.unitmesh.genius.prompt.PromptFactory

class GitHubActionPromptFactory : PromptFactory("prompts/cicd/github-action") {
    override fun createPrompt(project: GeniusProject, description: String): String {
        TODO("Not yet implemented")
    }
}