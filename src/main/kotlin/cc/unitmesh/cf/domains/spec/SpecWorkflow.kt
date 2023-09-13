package cc.unitmesh.cf.domains.spec

import cc.unitmesh.cf.core.flow.Workflow
import cc.unitmesh.cf.core.flow.model.ChatWebContext
import cc.unitmesh.cf.core.flow.model.StageContext
import cc.unitmesh.cf.core.flow.model.WorkflowResult
import io.reactivex.rxjava3.core.Flowable

class SpecWorkflow : Workflow() {
    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): Flowable<WorkflowResult> {
        TODO()
    }
}