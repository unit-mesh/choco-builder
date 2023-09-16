package cc.unitmesh.cf.domains.semantic

import cc.unitmesh.cf.core.flow.Workflow
import cc.unitmesh.cf.core.flow.model.Answer
import cc.unitmesh.cf.core.flow.model.ChatWebContext
import cc.unitmesh.cf.core.flow.model.StageContext
import cc.unitmesh.cf.core.flow.model.WorkflowResult
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.domains.SupportedDomains
import cc.unitmesh.cf.domains.interpreter.CodeInterpreterWorkflow
import cc.unitmesh.cf.domains.semantic.context.SemanticVariableResolver
import cc.unitmesh.cf.domains.semantic.flow.SemanticProblemAnalyzer
import cc.unitmesh.cf.domains.semantic.flow.SemanticSolutionExecutor
import cc.unitmesh.cf.domains.semantic.model.ExplainQuery
import cc.unitmesh.cf.infrastructure.llms.embedding.SentenceTransformersEmbedding
import cc.unitmesh.store.ElasticsearchStore
import io.reactivex.rxjava3.core.Flowable
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class CodeSemanticWorkflow(
    @Value("\${elasticsearch.uris}")
    private var elasticsearchUrl: String
) : Workflow() {
    @Autowired
    private lateinit var llmProvider: LlmProvider

    @Autowired
    private lateinit var variableResolver: SemanticVariableResolver

    val store: ElasticsearchStore = ElasticsearchStore(elasticsearchUrl)

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

        // todo: send to local

        log.info("Semantic analyze: {}", analyze)

        val answerFlowable: Flowable<Answer> =
            SemanticSolutionExecutor(llmProvider, store, embedding, variableResolver).execute(analyze)

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
                |3. `englishQuery` is a reference to the document that you think is the answer to the question.
                |4. `originLanguageQuery` 是从用户的问题中提取出来的自然语言查询，以用于查询用户的问题。
                |5. `hypotheticalDocument` ia a code snippet that could hypothetically be returned by a code search engine as the answer.
                |5.
                | 
                |For examples:
                |
                |""".trimMargin(),
            examples = ExplainQuery.QAExamples,
        )
        val EXECUTE: StageContext = StageContext(
            id = "SemanticExecute",
            stage = StageContext.Stage.Execute,
            systemPrompt = """Your job is to answer a query about a codebase using the information above.
                |
                |You must use the following formatting rules at all times:
                |  - Provide only as much information and code as is necessary to answer the query and be concise
                |  - If you do not have enough information needed to answer the query, do not make up an answer
                |  - When referring to code, you must provide an example in a code block
                |  - Keep number of quoted lines of code to a minimum when possible
                |  - Basic markdown is allowed
                | 
                |相关的代码：
                |${'$'}{relevantCode} 
                |
                |用户的问题：${'$'}{question}
                |
                |""".trimMargin()
        );
    }
}
