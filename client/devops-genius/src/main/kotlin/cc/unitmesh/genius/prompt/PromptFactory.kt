package cc.unitmesh.genius.prompt

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.genius.project.GeniusProject

abstract class PromptFactory(promptsBasePath: String) {
    protected val promptsLoader: PromptsLoader

    init {
        promptsLoader = PromptsLoader(promptsBasePath)
    }

    abstract fun createPrompt(project: GeniusProject, description: String): List<LlmMsg.ChatMessage>
}