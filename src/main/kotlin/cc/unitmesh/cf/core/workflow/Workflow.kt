package cc.unitmesh.cf.core.workflow

import cc.unitmesh.cf.core.process.AnalyzeResult
import cc.unitmesh.cf.core.process.ClarifyResult
import cc.unitmesh.cf.core.process.DesignResult
import cc.unitmesh.cf.core.process.ExecuteResult
import cc.unitmesh.cf.presentation.domain.ChatWebContext

abstract class Workflow {
    val chatWebContext: ChatWebContext? = null

    /**
     * save prompt list for debug in GUI
     */
    open val prompts: LinkedHashMap<StageContext.Stage, StageContext> = linkedMapOf()

    abstract fun execute(prompt: StageContext, chatWebContext: ChatWebContext): WorkflowResult?

    open fun result(stage: StageContext.Stage, result: Any): WorkflowResult? {
        return when (stage) {
            StageContext.Stage.Classify -> TODO()
            StageContext.Stage.Clarify -> WorkflowResult(stage, result.javaClass, result as ClarifyResult)
            StageContext.Stage.Analyze -> WorkflowResult(stage, result.javaClass, result as AnalyzeResult)
            StageContext.Stage.Design -> WorkflowResult(stage, result.javaClass, result as DesignResult)
            StageContext.Stage.Execute -> WorkflowResult(stage, result.javaClass, result as ExecuteResult)
            StageContext.Stage.Custom -> TODO()
        }
    }
}