package cc.unitmesh.cf.domains.frontend.flow

import cc.unitmesh.cf.core.base.FlowActionFlag
import cc.unitmesh.cf.core.process.ProblemClarifier
import cc.unitmesh.cf.domains.frontend.FEWorkflow
import cc.unitmesh.cf.domains.frontend.context.FEVariableResolver
import cc.unitmesh.cf.domains.frontend.context.FEDslContextBuilder
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider
import cc.unitmesh.cf.core.llms.LlmMsg

class FEProblemClarifier(
    private val contextBuilder: FEDslContextBuilder,
    private val completion: LlmProvider,
    private val variable: FEVariableResolver,
) : ProblemClarifier {
    private val clarifyContext = FEWorkflow.CLARIFY

    companion object {
        val log = org.slf4j.LoggerFactory.getLogger(FEProblemClarifier::class.java)
    }

    override fun clarify(
        domain: String,
        question: String,
        histories: List<String>,
    ): Pair<FlowActionFlag, String> {
        variable.updateQuestion(question)
        variable.histories(histories)

        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, variable.compile(clarifyContext.format())),
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, clarifyContext.questionPrefix + question),
        ).filter { it.content.isNotBlank() }

        log.info("Clarify messages: {}", messages)
        val completion = completion.simpleCompletion(messages)
        log.info("Clarify completion: {}", completion)
        return FlowActionFlag.parse(completion)
    }
}