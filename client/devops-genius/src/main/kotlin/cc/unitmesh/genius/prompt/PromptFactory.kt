package cc.unitmesh.genius.prompt

import cc.unitmesh.genius.project.GeniusProject

abstract class PromptFactory(promptsBasePath: String) {
    private val promptsLoader: PromptsLoader

    init {
        promptsLoader = PromptsLoader(promptsBasePath)
    }

    protected fun loadPrompts(paths: List<String>): String {
        return paths.joinToString(System.lineSeparator())
    }

    abstract fun createPrompt(project: GeniusProject, description: String): String
}