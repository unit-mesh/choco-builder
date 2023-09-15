package cc.unitmesh.cf.domains.semantic

import cc.unitmesh.cf.core.flow.Workflow
import cc.unitmesh.cf.core.flow.model.ChatWebContext
import cc.unitmesh.cf.core.flow.model.StageContext
import cc.unitmesh.cf.core.flow.model.WorkflowResult
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.domains.interpreter.CodeInterpreterWorkflow
import cc.unitmesh.cf.domains.semantic.model.ExplainQuery
import io.reactivex.rxjava3.core.Flowable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CodeSemanticWorkflow : Workflow() {
    @Autowired
    private lateinit var llmProvider: LlmProvider

    override val prompts: LinkedHashMap<StageContext.Stage, StageContext>
        get() = linkedMapOf(
            ANALYSIS.stage to ANALYSIS
        )

    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): Flowable<WorkflowResult> {
        TODO("Not yet implemented")
    }

    companion object {
        val log = org.slf4j.LoggerFactory.getLogger(CodeInterpreterWorkflow::class.java)
        val ANALYSIS: StageContext = StageContext(
            id = "FrontendExecute",
            stage = StageContext.Stage.Analyze,
            systemPrompt = """Your are a senior software developer, your job is to transpile user's question relative to codebase.
                |
                |1. YOU MUST follow the DSL format.
                |2. You MUST translate user's question into a DSL query.
                |3. `query` is a reference to the document that you think is the answer to the question.
                |4. `hypothetical_document` is a example of the document that you think is the answer to the question.
                | 
                |For examples:
                |
                |""".trimMargin(),
            examples = ExplainQuery.QAExamples,
        )
    }
}
