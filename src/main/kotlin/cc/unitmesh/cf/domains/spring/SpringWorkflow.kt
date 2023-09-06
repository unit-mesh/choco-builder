package cc.unitmesh.cf.domains.spring

import cc.unitmesh.cf.core.workflow.StageContext
import cc.unitmesh.cf.core.workflow.Workflow
import cc.unitmesh.cf.core.workflow.WorkflowResult
import cc.unitmesh.cf.presentation.domain.ChatWebContext

class SpringWorkflow : Workflow() {
    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): WorkflowResult? {
        TODO("Not yet implemented")
    }

}
