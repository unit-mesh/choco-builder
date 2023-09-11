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

class FESolutionExecutor(
    private val contextBuilder: FEDslContextBuilder,
    private val completion: LlmProvider,
    private val variable: FEVariableResolver,
) : SolutionExecutor<UiPage> {
    override val interpreters: List<Interpreter> = listOf()

    companion object {
        val log = org.slf4j.LoggerFactory.getLogger(FESolutionExecutor::class.java)
    }

    override fun execute(solution: UiPage): Answer {
        val basePrompt = FEWorkflow.EXECUTE.format()
        variable.put("userLayout", solution.layout)
        val components = variable.getComponents()

        val userComponents: MutableList<String> = mutableListOf()
        solution.components.filter { it.isNotBlank() }.forEach {
            val component = components.find { c -> c.tagName.lowercase() == it.lowercase() }
            if (component != null && component.examples.isNotEmpty()) {
                userComponents += "${component.name}\n: ```design\n${component.examples[0].content}\n```"
            }
        }
        variable.put("userComponents", userComponents.joinToString(separator = "\n"))

        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, variable.compile(basePrompt)),
        ).filter { it.content.isNotBlank() }

        log.info("Execute messages: {}", messages)
        val completion = completion.simpleCompletion(messages)
        log.info("Execute completion: {}", completion)

        return object : Answer {
            override var executor: String = ""
            override var values: Any = completion
        }
    }
}