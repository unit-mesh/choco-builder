package cc.unitmesh.cf.domains.semantic.flow

import cc.unitmesh.cf.core.flow.ProblemAnalyzer
import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.domains.semantic.CodeSemanticWorkflow
import cc.unitmesh.cf.domains.semantic.model.ExplainQuery

class SemanticProblemAnalyzer(
    private val completion: LlmProvider
): ProblemAnalyzer {
    companion object {
        val log = org.slf4j.LoggerFactory.getLogger(SemanticProblemAnalyzer::class.java)
    }
    override fun analyze(domain: String, question: String): ExplainQuery {
        val stageContext = CodeSemanticWorkflow.ANALYSIS
        val systemPrompt = stageContext.format()

        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, systemPrompt),
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, question),
        ).filter { it.content.isNotBlank() }

        log.info("Clarify messages: {}", messages)
        val completion = completion.completion(messages)
        log.info("Clarify completion: {}", completion)
        return ExplainQuery.parse(question, completion)
    }

}