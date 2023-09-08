package cc.unitmesh.cf.domains.sql

import cc.unitmesh.cf.core.workflow.StageContext
import cc.unitmesh.cf.core.workflow.Workflow
import cc.unitmesh.cf.core.workflow.WorkflowResult
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider
import cc.unitmesh.cf.presentation.domain.ChatWebContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SqlWorkflow() : Workflow() {
    @Autowired
    private lateinit var llmProvider: LlmProvider

    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): WorkflowResult? {
        TODO("Not yet implemented")
    }
}
