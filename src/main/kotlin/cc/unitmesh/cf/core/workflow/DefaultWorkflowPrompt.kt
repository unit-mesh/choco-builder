package cc.unitmesh.cf.core.workflow

import cc.unitmesh.cf.presentation.domain.ChatWebContext

class DefaultWorkflow : Workflow() {
    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): WorkflowResult? {
        return null
    }
}
