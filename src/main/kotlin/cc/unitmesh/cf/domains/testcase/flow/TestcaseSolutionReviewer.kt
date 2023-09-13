package cc.unitmesh.cf.domains.testcase.flow

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.cf.core.flow.SolutionReviewer
import cc.unitmesh.cf.domains.testcase.TestcaseDomainDecl
import cc.unitmesh.cf.domains.testcase.TestcaseWorkflow
import cc.unitmesh.cf.domains.testcase.context.TestcaseVariableResolver
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.core.llms.LlmMsg

class TestcaseSolutionReviewer(
    private val completion: LlmProvider,
    private val variable: TestcaseVariableResolver,
) : SolutionReviewer {
    override fun review(question: String, histories: List<String>): Dsl {
        variable.put("testcases", histories.last())

        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, variable.compile(TestcaseWorkflow.REVIEW.format())),
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, question),
        ).filter { it.content.isNotBlank() }

        log.info("messages: {}", messages)
        val completion = completion.completion(messages)
        log.info("completion: {}", completion)

        return object : Dsl {
            override var domain: String = TestcaseDomainDecl.DOMAIN
            override val content: String = completion
            override var interpreters: List<DslInterpreter> = listOf()
        }
    }

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(TestcaseSolutionReviewer::class.java)
    }
}