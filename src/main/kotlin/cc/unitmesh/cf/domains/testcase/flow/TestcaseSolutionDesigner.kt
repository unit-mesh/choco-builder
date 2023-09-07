package cc.unitmesh.cf.domains.testcase.flow

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.cf.core.process.SolutionDesigner
import cc.unitmesh.cf.domains.testcase.TestcaseWorkflow
import cc.unitmesh.cf.domains.testcase.context.TestcaseVariableResolver
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider
import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg

class TestcaseSolutionDesigner(
    private val completion: LlmProvider,
    private val variable: TestcaseVariableResolver,
) : SolutionDesigner {
    override fun design(domain: String, question: String, histories: List<String>): Dsl {
        variable.updateQuestion(question)
        variable.updateHistories(histories)

        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, variable.compile(TestcaseWorkflow.DESIGN.format())),
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, question),
        ).filter { it.content.isNotBlank() }

        log.info("messages: {}", messages)
        val completion = completion.simpleCompletion(messages)
        log.info("completion: {}", completion)

        return object : Dsl {
            override var domain: String = domain
            override val content: String = completion
            override var interpreters: List<DslInterpreter> = listOf()
        }
    }

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(TestcaseSolutionDesigner::class.java)
    }
}