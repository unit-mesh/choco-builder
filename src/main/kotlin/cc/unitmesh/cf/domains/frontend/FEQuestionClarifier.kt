package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.core.base.ClarificationAction
import cc.unitmesh.cf.core.process.QuestionClarifier
import cc.unitmesh.cf.domains.frontend.dsl.FEDslContextBuilder
import cc.unitmesh.cf.domains.frontend.prompt.FEWorkflow
import cc.unitmesh.cf.infrastructure.llms.completion.CompletionProvider
import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg

class FEQuestionClarifier(
    private val contextBuilder: FEDslContextBuilder,
    private val completion: CompletionProvider,
) : QuestionClarifier {
    override fun clarify(
        domain: String,
        question: String,
        histories: List<String>,
    ): Pair<ClarificationAction, String> {
        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, FEWorkflow.CLARIFY.forTestFormat()),
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, "你必须按格式输出！"),
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, question),
        ).filter { it.content.isNotBlank() }

        val completion = completion.createCompletion(messages)
        return ClarificationAction.parse(completion.content)
    }
}