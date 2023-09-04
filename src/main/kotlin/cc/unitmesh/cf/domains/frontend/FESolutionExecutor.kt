package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.core.base.Answer
import cc.unitmesh.cf.core.base.ClarificationAction
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.Interpreter
import cc.unitmesh.cf.core.process.SolutionExecutor
import cc.unitmesh.cf.domains.frontend.context.FEWorkflow
import cc.unitmesh.cf.domains.frontend.dsl.FEDslContextBuilder
import cc.unitmesh.cf.domains.frontend.model.UiPage
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider
import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg

class FESolutionExecutor(
    private val contextBuilder: FEDslContextBuilder,
    private val completion: LlmProvider,
) : SolutionExecutor<UiPage> {
    override val interpreters: List<Interpreter> = listOf()

    override fun execute(solution: UiPage): Answer {
        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, FEWorkflow.EXECUTE.format()),
            // todo: compile with variablses
        ).filter { it.content.isNotBlank() }

        val completion = completion.createCompletion(messages)
        return object : Answer {
            override var executor: String = ""
            override var values: Any = completion.content
        }
    }
}