package cc.unitmesh.cf.domains.frontend.flow

import cc.unitmesh.cf.core.flow.SolutionDesigner
import cc.unitmesh.cf.domains.frontend.FEWorkflow
import cc.unitmesh.cf.domains.frontend.context.FEVariableResolver
import cc.unitmesh.cf.domains.frontend.context.FEDslContextBuilder
import cc.unitmesh.cf.domains.frontend.model.UiPage
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.core.llms.LlmMsg

class FESolutionDesigner(
    private val contextBuilder: FEDslContextBuilder,
    private val completion: LlmProvider,
    private val variable: FEVariableResolver,
) : SolutionDesigner {

    companion object {
        val log = org.slf4j.LoggerFactory.getLogger(FESolutionDesigner::class.java)
    }
    override fun design(domain: String, question: String, histories: List<String>): UiPage {
        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, variable.compile(FEWorkflow.DESIGN.format())),
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, question),
        ).filter { it.content.isNotBlank() }

        log.info("Designer messages: {}", messages)
        val completion = completion.simpleCompletion(messages)
        log.info("Designer completion: {}", completion)
        return UiPage.parse(completion)
    }
}