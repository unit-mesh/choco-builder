package cc.unitmesh.cf.domains.testcase

import cc.unitmesh.cf.core.workflow.StageContext
import cc.unitmesh.cf.core.workflow.Workflow
import cc.unitmesh.cf.core.workflow.WorkflowResult
import cc.unitmesh.cf.presentation.domain.ChatWebContext

class TestcaseWorkflow : Workflow() {
    override val prompts: LinkedHashMap<StageContext.Stage, StageContext> = linkedMapOf()

    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): WorkflowResult? {
        TODO("Not yet implemented")
    }
}
