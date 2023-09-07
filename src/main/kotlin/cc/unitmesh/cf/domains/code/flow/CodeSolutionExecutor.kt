package cc.unitmesh.cf.domains.code.flow

import cc.unitmesh.cf.core.base.Answer
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.cf.core.dsl.Interpreter
import cc.unitmesh.cf.core.process.SolutionExecutor
import cc.unitmesh.cf.domains.code.CodeInterpreterWorkflow
import cc.unitmesh.cf.domains.code.CodeInterpreter
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider
import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg
import cc.unitmesh.cf.infrastructure.parser.MarkdownCode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CodeSolutionExecutor(
    private val completion: LlmProvider,
    private val codeInterpreter: CodeInterpreter,
) : SolutionExecutor<CodeInput> {
    override val interpreters: List<Interpreter> = listOf()

    companion object {
        val log = org.slf4j.LoggerFactory.getLogger(CodeSolutionExecutor::class.java)
    }

    override fun execute(solution: CodeInput): Answer {
        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, CodeInterpreterWorkflow.EXECUTE.format()),
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, solution.content),
        ).filter { it.content.isNotBlank() }

        log.info("Execute messages: {}", messages)
        val completion = completion.simpleCompletion(messages)
        log.info("Execute completion: {}", completion)

        var evalResult = ""

        val code = MarkdownCode.parse(completion)
        if (code.language.lowercase() == "kotlin") {
            val result = codeInterpreter.interpret(CodeInput(content = code.text));

            log.info("Execute result: {}", result)
            evalResult = Json.encodeToString(result)
        }

        return object : Answer {
            override var executor: String = ""
            override var values: Any = "$completion\n\n```interpreter\n$evalResult\n```\n"
        }
    }

}

class CodeInput(
    override val content: String = "",
    override var domain: String = "code-interpreter",
) : Dsl {
    override var interpreters: List<DslInterpreter> = listOf()
}
