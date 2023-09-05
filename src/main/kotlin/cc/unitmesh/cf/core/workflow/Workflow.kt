package cc.unitmesh.cf.core.workflow

import cc.unitmesh.cf.core.process.AnalyzeResult
import cc.unitmesh.cf.core.process.ClarifyResult
import cc.unitmesh.cf.core.process.DesignResult
import cc.unitmesh.cf.core.process.ExecuteResult
import cc.unitmesh.cf.core.prompt.PromptWithStage
import cc.unitmesh.cf.presentation.domain.ChatWebContext

abstract class Workflow {
    val chatWebContext: ChatWebContext? = null

    /**
     * save prompt list for debug in GUI
     */
    open val prompts: LinkedHashMap<PromptWithStage.Stage, PromptWithStage> = linkedMapOf()

    abstract fun execute(prompt: PromptWithStage, chatWebContext: ChatWebContext): WorkflowResult?

    open fun result(stage: PromptWithStage.Stage, result: Any): WorkflowResult? {
        return when (stage) {
            PromptWithStage.Stage.Classify -> TODO()
            PromptWithStage.Stage.Clarify -> WorkflowResult(stage, result.javaClass, result as ClarifyResult)
            PromptWithStage.Stage.Analyze -> WorkflowResult(stage, result.javaClass, result as AnalyzeResult)
            PromptWithStage.Stage.Design -> WorkflowResult(stage, result.javaClass, result as DesignResult)
            PromptWithStage.Stage.Execute -> WorkflowResult(stage, result.javaClass, result as ExecuteResult)
            PromptWithStage.Stage.Custom -> TODO()
        }
    }
}