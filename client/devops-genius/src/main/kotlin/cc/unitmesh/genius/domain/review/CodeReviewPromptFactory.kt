package cc.unitmesh.genius.domain.review

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.genius.project.GeniusProject
import cc.unitmesh.genius.prompt.PromptFactory
import cc.unitmesh.prompt.executor.TemplateRoleSplitter
import cc.unitmesh.prompt.executor.mapToMessages
import cc.unitmesh.prompt.template.VelocityCompiler

class CodeReviewPromptFactory(var context: CodeReviewContext = CodeReviewContext()) : PromptFactory("code-review") {
    private val template = VelocityCompiler()
    private val splitter = TemplateRoleSplitter()

    override fun createPrompt(project: GeniusProject, description: String): List<LlmMsg.ChatMessage> {
        val prompt = promptsLoader.getTemplate("simple-review.open-ai.vm")

        val msgs = splitter.split(prompt)
        val messages = mapToMessages(msgs).toMutableList()

        messages.map {
            if (it.role == LlmMsg.ChatRole.User) {
                template.append("context", context)
                it.content = template.compileToString(it.content)
            }
        }

        return messages
    }
}
