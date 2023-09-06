package cc.unitmesh.cf.domains.code.flow

import cc.unitmesh.cf.core.base.Answer
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.cf.core.dsl.Interpreter
import cc.unitmesh.cf.core.process.SolutionExecutor
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider
import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg

class CodeSolutionExecutor(
    private val completion: LlmProvider,
) : SolutionExecutor<CodeInput> {
    override val interpreters: List<Interpreter> = listOf()
    companion object {
        val log = org.slf4j.LoggerFactory.getLogger(CodeSolutionExecutor::class.java)
    }

    override fun execute(solution: CodeInput): Answer {
        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, solution.content),
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

class CodeInput(
    override var domain: String = "code-interpreter",
    val question: String,
) : Dsl {
    override val content: String = ""
    override var interpreters: List<DslInterpreter> = listOf()
}
