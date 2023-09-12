package cc.unitmesh.cf.domains.sql

import cc.unitmesh.cf.core.flow.Workflow
import cc.unitmesh.cf.core.flow.model.ChatWebContext
import cc.unitmesh.cf.core.flow.model.StageContext
import cc.unitmesh.cf.core.flow.model.WorkflowResult
import cc.unitmesh.cf.core.llms.LlmProvider
import io.reactivex.rxjava3.core.Flowable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SqlWorkflow() : Workflow() {
    @Autowired
    private lateinit var llmProvider: LlmProvider

    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): Flowable<WorkflowResult> {
        TODO("Not yet implemented")
    }
}
