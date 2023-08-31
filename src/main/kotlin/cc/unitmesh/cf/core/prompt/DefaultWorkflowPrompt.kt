package cc.unitmesh.cf.core.prompt

import cc.unitmesh.cf.core.prompt.PromptTemplate.*

open class Workflow() {
    open val prompts: LinkedHashMap<Phase, PromptTemplate> = linkedMapOf()
}

class DefaultWorkflow : Workflow() {
    override val prompts = linkedMapOf(
        Phase.Classify to PromptTemplate(phase = Phase.Classify, systemPrompt = ""),
        Phase.Clarify to PromptTemplate(phase = Phase.Clarify, systemPrompt = ""),
        Phase.Analyze to PromptTemplate(phase = Phase.Analyze, systemPrompt = ""),
        Phase.Design to PromptTemplate(phase = Phase.Design, systemPrompt = ""),
        Phase.Execute to PromptTemplate(phase = Phase.Execute, systemPrompt = ""),
    )
}
