package cc.unitmesh.genius.prompt

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.genius.project.GeniusProject
import cc.unitmesh.template.TemplateRoleSplitter
import cc.unitmesh.prompt.template.VelocityCompiler

abstract class PromptFactory(promptsBasePath: String) {
    protected val template = VelocityCompiler()
    protected val promptsLoader: PromptsLoader
    protected val splitter = TemplateRoleSplitter()
    protected open val context: Any = ""

    open val templatePath = ""

    init {
        promptsLoader = PromptsLoader(promptsBasePath)
    }

    open fun createPrompt(project: GeniusProject, description: String): List<LlmMsg.ChatMessage> {
        val prompt = promptsLoader.getTemplate(templatePath)

        val msgs = splitter.split(prompt)
        val messages = LlmMsg.fromMap(msgs).toMutableList()

        messages.map {
            if (it.role == LlmMsg.ChatRole.User) {
                template.append("context", context)
                it.content = template.compileToString(it.content)
            }
        }

        return messages
    }
}