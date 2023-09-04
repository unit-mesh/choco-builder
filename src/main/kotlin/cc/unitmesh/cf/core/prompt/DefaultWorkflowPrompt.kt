package cc.unitmesh.cf.core.prompt

import cc.unitmesh.cf.core.prompt.PromptTemplate.*

open class Workflow() {
    open val prompts: LinkedHashMap<Stage, PromptTemplate> = linkedMapOf()
}

class DefaultWorkflow : Workflow() {
    override val prompts = linkedMapOf(
        Stage.Classify to PromptTemplate(stage = Stage.Classify, systemPrompt = ""),
        Stage.Clarify to PromptTemplate(stage = Stage.Clarify, systemPrompt = ""),
        Stage.Analyze to PromptTemplate(stage = Stage.Analyze, systemPrompt = ""),
        Stage.Design to PromptTemplate(stage = Stage.Design, systemPrompt = ""),
        Stage.Execute to PromptTemplate(stage = Stage.Execute, systemPrompt = ""),
    )
}
