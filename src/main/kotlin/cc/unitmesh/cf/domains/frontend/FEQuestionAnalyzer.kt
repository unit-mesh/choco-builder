package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.core.process.QuestionAnalyzer
import cc.unitmesh.cf.domains.frontend.context.FEWorkflow
import cc.unitmesh.cf.domains.frontend.dsl.FEDslContextBuilder
import cc.unitmesh.cf.domains.frontend.model.UiPage
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider
import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg

class FEQuestionAnalyzer(
    private val contextBuilder: FEDslContextBuilder,
    private val completion: LlmProvider,
) : QuestionAnalyzer {
    override fun analyze(domain: String, question: String): UiPage {
        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, FEWorkflow.DESIGN.format()),
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, question),
        ).filter { it.content.isNotBlank() }

        val completion = completion.createCompletion(messages)
        return UiPage.parse(completion.content)
    }
}