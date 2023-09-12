package cc.unitmesh.cf.domains.frontend.flow

import cc.unitmesh.cf.core.flow.model.Answer
import cc.unitmesh.cf.core.dsl.Interpreter
import cc.unitmesh.cf.core.flow.SolutionExecutor
import cc.unitmesh.cf.domains.frontend.FEWorkflow
import cc.unitmesh.cf.domains.frontend.context.FEVariableResolver
import cc.unitmesh.cf.domains.frontend.context.FEDslContextBuilder
import cc.unitmesh.cf.domains.frontend.model.UiPage
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.core.llms.LlmMsg
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable

class FESolutionExecutor(
    private val contextBuilder: FEDslContextBuilder,
    private val completion: LlmProvider,
    private val variable: FEVariableResolver,
) : SolutionExecutor<UiPage> {
    override val interpreters: List<Interpreter> = listOf()

    companion object {
        val log = org.slf4j.LoggerFactory.getLogger(FESolutionExecutor::class.java)
    }

    override fun execute(solution: UiPage): Flowable<Answer> {
        val basePrompt = FEWorkflow.EXECUTE.format()
        variable.put("userLayout", solution.layout)

        val userComponents: MutableList<String> = prepareRelatedComponents(solution)
        variable.put("userComponents", userComponents.joinToString(separator = "\n"))

        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, variable.compile(basePrompt)),
        ).filter { it.content.isNotBlank() }

        log.info("Execute messages: {}", messages)
        val completion: Flowable<String> = completion.flowCompletion(messages)
        log.info("Execute completion: {}", completion)

        return Flowable.create({ emitter ->
            completion.subscribe({
                val answer = Answer(this.javaClass.name, it)
                emitter.onNext(answer)
            }, {
                emitter.onError(it)
            }, {
                emitter.onComplete()
            })
        }, BackpressureStrategy.BUFFER)
    }

    private fun prepareRelatedComponents(solution: UiPage): MutableList<String> {
        val components = variable.getComponents()
        val userComponents: MutableList<String> = mutableListOf()
        solution.components.filter { it.isNotBlank() }.forEach {
            val component = components.find { c -> c.tagName.lowercase() == it.lowercase() }
            if (component != null && component.examples.isNotEmpty()) {
                userComponents += "${component.name} component:\n```design\n${component.examples[0].content}\n```"
            }
        }
        return userComponents
    }
}