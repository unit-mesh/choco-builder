package cc.unitmesh.cf.domains.semantic

import cc.unitmesh.cf.core.flow.Workflow
import cc.unitmesh.cf.core.flow.model.Answer
import cc.unitmesh.cf.core.flow.model.ChatWebContext
import cc.unitmesh.cf.core.flow.model.StageContext
import cc.unitmesh.cf.core.flow.model.WorkflowResult
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.domains.SupportedDomains
import cc.unitmesh.cf.domains.interpreter.CodeInterpreterWorkflow
import cc.unitmesh.cf.domains.semantic.flow.SemanticProblemAnalyzer
import cc.unitmesh.cf.domains.semantic.flow.SemanticSolutionExecutor
import cc.unitmesh.cf.domains.semantic.model.ExplainQuery
import cc.unitmesh.cf.infrastructure.llms.embedding.SentenceTransformersEmbedding
import cc.unitmesh.store.ElasticsearchStore
import io.reactivex.rxjava3.core.Flowable
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CodeSemanticWorkflow : Workflow() {
    @Autowired
    private lateinit var llmProvider: LlmProvider

    val store: ElasticsearchStore = ElasticsearchStore()
    val embedding = SentenceTransformersEmbedding()

    override val stages: LinkedHashMap<StageContext.Stage, StageContext>
        get() = linkedMapOf(
            ANALYSIS.stage to ANALYSIS,
            EXECUTE.stage to EXECUTE,
        )

    val domainName = SupportedDomains.CodeSemanticSearch.value

    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): Flowable<WorkflowResult> {
        val question = chatWebContext.messages.last().content
        val analyze = SemanticProblemAnalyzer(llmProvider)
            .analyze(domainName, question)

        val answerFlowable: Flowable<Answer> =
            SemanticSolutionExecutor(llmProvider, store, embedding).execute(analyze)

        return toFlowableResult(answerFlowable)
    }

    companion object {
        val log: Logger = org.slf4j.LoggerFactory.getLogger(CodeInterpreterWorkflow::class.java)
        val ANALYSIS: StageContext = StageContext(
            id = "SemanticAnalyze",
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
        val EXECUTE: StageContext = StageContext(
            id = "SemanticExecute",
            stage = StageContext.Stage.Execute,
            systemPrompt = """""",
        );
    }
}
