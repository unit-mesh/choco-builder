package cc.unitmesh.genius.prompt

import cc.unitmesh.genius.project.SimpleProject

abstract class PromptFactory(promptsBasePath: String) {
    private val promptsLoader: PromptsLoader

    init {
        promptsLoader = PromptsLoader(promptsBasePath)
    }

    private fun loadPrompts(paths: List<String?>): String {
        return paths.joinToString(System.lineSeparator())
    }

    abstract fun createPrompt(project: SimpleProject, description: String): String
}