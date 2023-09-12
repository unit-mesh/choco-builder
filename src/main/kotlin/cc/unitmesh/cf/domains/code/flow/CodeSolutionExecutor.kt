package cc.unitmesh.cf.domains.code.flow

import cc.unitmesh.cf.core.flow.model.Answer
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.cf.core.dsl.Interpreter
import cc.unitmesh.cf.core.flow.SolutionExecutor
import cc.unitmesh.cf.domains.SupportedDomains
import cc.unitmesh.cf.domains.code.CodeInterpreter
import cc.unitmesh.cf.domains.code.CodeInterpreterWorkflow
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.cf.core.parser.MarkdownCode
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.Logger

class CodeSolutionExecutor(
    private val completion: LlmProvider,
    private val codeInterpreter: CodeInterpreter,
) : SolutionExecutor<CodeInput> {
    override val interpreters: List<Interpreter> = listOf()

    companion object {
        val log: Logger = org.slf4j.LoggerFactory.getLogger(CodeSolutionExecutor::class.java)
    }

    override fun execute(solution: CodeInput): Flowable<Answer> {
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

            evalResult = Json.encodeToString(result)
            log.info("Execute result: {}", evalResult)
        }

        return Flowable.just(Answer("", "$completion\n\n```interpreter\n$evalResult\n```\n"))
    }

}

class CodeInput(
    override val content: String = "",
    override var domain: String = SupportedDomains.CodeInterpreter.value,
) : Dsl {
    override var interpreters: List<DslInterpreter> = listOf()
}
