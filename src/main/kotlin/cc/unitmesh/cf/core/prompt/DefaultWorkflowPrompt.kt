package cc.unitmesh.cf.core.prompt

import cc.unitmesh.cf.core.prompt.PromptWithStage.*

open class Workflow() {
    /**
     * save prompt list for debug in GUI
     */
    open val prompts: LinkedHashMap<Stage, PromptWithStage> = linkedMapOf()

    fun execute(stage: Stage) {

    }
}

class DefaultWorkflow : Workflow()
