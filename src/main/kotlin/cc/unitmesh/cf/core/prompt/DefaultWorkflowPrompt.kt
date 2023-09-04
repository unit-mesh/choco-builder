package cc.unitmesh.cf.core.prompt

import cc.unitmesh.cf.core.prompt.PromptWithStage.*

open class Workflow() {
    open val prompts: LinkedHashMap<Stage, PromptWithStage> = linkedMapOf()
}

class DefaultWorkflow : Workflow()
