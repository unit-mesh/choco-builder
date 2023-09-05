package cc.unitmesh.cf.core.workflow

import cc.unitmesh.cf.core.prompt.PromptWithStage
import cc.unitmesh.cf.presentation.domain.ChatWebContext

class DefaultWorkflow : Workflow() {
    override fun execute(prompt: PromptWithStage, chatWebContext: ChatWebContext): WorkflowResult? {
        return null
    }
}
