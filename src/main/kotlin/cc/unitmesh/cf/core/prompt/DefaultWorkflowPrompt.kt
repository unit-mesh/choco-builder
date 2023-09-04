package cc.unitmesh.cf.core.prompt

import cc.unitmesh.cf.core.prompt.PromptWithStage.*

open class Workflow() {
    open val prompts: LinkedHashMap<Stage, PromptWithStage> = linkedMapOf()
}

class DefaultWorkflow : Workflow() {
    override val prompts = linkedMapOf(
        Stage.Classify to PromptWithStage(stage = Stage.Classify, systemPrompt = ""),
        Stage.Clarify to PromptWithStage(stage = Stage.Clarify, systemPrompt = ""),
        Stage.Analyze to PromptWithStage(stage = Stage.Analyze, systemPrompt = ""),
        Stage.Design to PromptWithStage(stage = Stage.Design, systemPrompt = ""),
        Stage.Execute to PromptWithStage(stage = Stage.Execute, systemPrompt = ""),
    )
}
