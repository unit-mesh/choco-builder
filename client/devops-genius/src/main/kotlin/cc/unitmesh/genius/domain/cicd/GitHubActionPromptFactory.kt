package cc.unitmesh.genius.domain.cicd

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.genius.project.GeniusProject
import cc.unitmesh.genius.prompt.PromptFactory

class GitHubActionPromptFactory : PromptFactory("prompts/cicd/github-action") {
    override fun createPrompt(project: GeniusProject, description: String): List<LlmMsg.ChatMessage> {
        TODO("Not yet implemented")
    }
}