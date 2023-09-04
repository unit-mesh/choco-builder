package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.core.base.Answer
import cc.unitmesh.cf.core.dsl.Interpreter
import cc.unitmesh.cf.core.process.SolutionExecutor
import cc.unitmesh.cf.domains.frontend.context.FEVariableResolver
import cc.unitmesh.cf.domains.frontend.context.FEDslContextBuilder
import cc.unitmesh.cf.domains.frontend.model.UiPage
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider
import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg

class FESolutionExecutor(
    private val contextBuilder: FEDslContextBuilder,
    private val completion: LlmProvider,
    private val variable: FEVariableResolver,
) : SolutionExecutor<UiPage> {
    override val interpreters: List<Interpreter> = listOf()

    override fun execute(solution: UiPage): Answer {
        val basePrompt = FEWorkflow.EXECUTE.format()
        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, variable.compile(basePrompt)),
        ).filter { it.content.isNotBlank() }

        val completion = completion.createCompletion(messages)
        return object : Answer {
            override var executor: String = ""
            override var values: Any = completion.content
        }
    }
}