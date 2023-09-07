package cc.unitmesh.cf.domains.testcase.flow

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.cf.core.process.ProblemAnalyzer
import cc.unitmesh.cf.domains.testcase.TestcaseDomainDecl
import cc.unitmesh.cf.domains.testcase.TestcaseWorkflow
import cc.unitmesh.cf.domains.testcase.context.TestcaseVariableResolver
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider
import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg

class TestcaseProblemAnalyzer(
    private val completion: LlmProvider,
    private val variable: TestcaseVariableResolver,
) : ProblemAnalyzer {
    override fun analyze(domain: String, question: String): Dsl {
        variable.updateQuestion(question)

        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, variable.compile(TestcaseWorkflow.ANALYZE.format())),
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, question),
        ).filter { it.content.isNotBlank() }

        val completion = completion.simpleCompletion(messages)
        return object : Dsl {
            override var domain: String = TestcaseDomainDecl.DOMAIN
            override val content: String = completion
            override var interpreters: List<DslInterpreter> = listOf()
        }
    }
}