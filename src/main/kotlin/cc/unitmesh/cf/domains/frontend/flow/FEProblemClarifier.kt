package cc.unitmesh.cf.domains.frontend.flow

import cc.unitmesh.cf.core.base.ClarificationAction
import cc.unitmesh.cf.core.process.ProblemClarifier
import cc.unitmesh.cf.domains.frontend.FEWorkflow
import cc.unitmesh.cf.domains.frontend.context.FEVariableResolver
import cc.unitmesh.cf.domains.frontend.context.FEDslContextBuilder
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider
import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg

class FEProblemClarifier(
    private val contextBuilder: FEDslContextBuilder,
    private val completion: LlmProvider,
    private val variable: FEVariableResolver,
) : ProblemClarifier {
    override fun clarify(
        domain: String,
        question: String,
        histories: List<String>,
    ): Pair<ClarificationAction, String> {
        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, variable.compile(FEWorkflow.CLARIFY.format())),
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, "你必须按格式输出！"),
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, question),
        ).filter { it.content.isNotBlank() }

        val completion = completion.createCompletion(messages)
        return ClarificationAction.parse(completion.content)
    }
}