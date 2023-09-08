package cc.unitmesh.cf.domains.testcase.flow

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.cf.core.process.ProblemAnalyzer
import cc.unitmesh.cf.domains.testcase.TestcaseDomainDecl
import cc.unitmesh.cf.domains.testcase.TestcaseWorkflow
import cc.unitmesh.cf.domains.testcase.context.TestcaseVariableResolver
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider
import cc.unitmesh.cf.infrastructure.llms.completion.TemperatureMode
import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg

class TestcaseProblemAnalyzer(
    private val completion: LlmProvider,
    private val variable: TestcaseVariableResolver,
    private val temperatureMode: TemperatureMode,
) : ProblemAnalyzer {
    override fun analyze(domain: String, question: String): Dsl {
        variable.updateQuestion(question)

        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, variable.compile(TestcaseWorkflow.ANALYZE.format())),
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, question),
        ).filter { it.content.isNotBlank() }

        log.info("temperatureMode: {}", temperatureMode);
        log.info("messages: {}", messages)

        val oldTemperatureMode = temperatureMode
        completion.setTemperatureMode(temperatureMode)
        val output = completion.simpleCompletion(messages)
        completion.setTemperatureMode(oldTemperatureMode)

        // TODO: add multiple temperatures support
        log.info("completion: {}", output)

        return object : Dsl {
            override var domain: String = TestcaseDomainDecl.DOMAIN
            override val content: String = output
            override var interpreters: List<DslInterpreter> = listOf()
        }
    }

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(TestcaseProblemAnalyzer::class.java)
    }
}