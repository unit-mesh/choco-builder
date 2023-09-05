package cc.unitmesh.cf.core.prompt

import cc.unitmesh.cf.core.process.AnalyzeResult
import cc.unitmesh.cf.core.process.ClarifyResult
import cc.unitmesh.cf.core.process.DesignResult
import cc.unitmesh.cf.core.process.ExecuteResult
import cc.unitmesh.cf.core.prompt.PromptWithStage.*
import cc.unitmesh.cf.presentation.domain.ChatWebContext

abstract class Workflow {
    val chatWebContext: ChatWebContext? = null

    /**
     * save prompt list for debug in GUI
     */
    open val prompts: LinkedHashMap<Stage, PromptWithStage> = linkedMapOf()

    open fun result(stage: Stage, result: Any): WorkflowResult {
        return when (stage) {
            Stage.Classify -> TODO()
            Stage.Clarify -> WorkflowResult(stage, result.javaClass, result as ClarifyResult)
            Stage.Analyze -> WorkflowResult(stage, result.javaClass, result as AnalyzeResult)
            Stage.Design -> WorkflowResult(stage, result.javaClass, result as DesignResult)
            Stage.Execute -> WorkflowResult(stage, result.javaClass, result as ExecuteResult)
            Stage.Custom -> TODO()
        }
    }

    abstract fun execute(prompt: PromptWithStage, chatWebContext: ChatWebContext): WorkflowResult
}

class WorkflowResult(
    val stage: Stage,
    val resultType: Class<*>,
    val result: Any,
)
