package cc.unitmesh.cf.domains.semantic.flow

import cc.unitmesh.cf.core.dsl.Interpreter
import cc.unitmesh.cf.core.flow.SolutionExecutor
import cc.unitmesh.cf.core.flow.model.Answer
import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.domains.frontend.flow.FESolutionExecutor
import cc.unitmesh.cf.domains.semantic.CodeSemanticWorkflow
import cc.unitmesh.cf.domains.semantic.context.SemanticVariableResolver
import cc.unitmesh.cf.domains.semantic.model.ExplainQuery
import cc.unitmesh.cf.infrastructure.llms.embedding.SentenceTransformersEmbedding
import cc.unitmesh.nlp.embedding.EncodingTokenizer
import cc.unitmesh.nlp.embedding.OpenAiEncoding
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.store.EmbeddingStore
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable

class SemanticSolutionExecutor(
    val completion: LlmProvider,
    val store: EmbeddingStore<Document>,
    val embedding: SentenceTransformersEmbedding,
    val variables: SemanticVariableResolver,
) : SolutionExecutor<ExplainQuery> {
    override val interpreters: List<Interpreter> = listOf()
    private val basePrompt = CodeSemanticWorkflow.EXECUTE.format()

    private val encodingTokenizer: EncodingTokenizer = OpenAiEncoding()

    override fun execute(solution: ExplainQuery): Flowable<Answer> {
        variables.putQuery(solution)
        val query = embedding.embed(solution.englishQuery)
        val originQuery = embedding.embed(solution.originLanguageQuery)
        val hypotheticalDocument = embedding.embed(solution.hypotheticalDocument)

        val hydeDocs = store.findRelevant(hypotheticalDocument, 15, 0.6)
        val list = store.findRelevant(query, 15, 0.6)
        val originLangList = store.findRelevant(originQuery, 15, 0.6)

        // remove duplicate in hydeDocs, list, originList
        val relevantDocuments = (hydeDocs + list + originLangList)
            .distinctBy { it.embedded.text }
            .sortedByDescending { it.score }

        var finalPrompt = basePrompt
        val codes: MutableList<String> = mutableListOf()
        relevantDocuments.forEach {
            // todo: add strategy for lost in the middle
            codes += it.embedded.text
            variables.putCode("", codes)
            val prompt = variables.compile(basePrompt)
            // todo: make 3072 configurable
            if (encodingTokenizer.encode(prompt).size < 3072) {
                finalPrompt = prompt
            }
        }

        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, finalPrompt),
        ).filter { it.content.isNotBlank() }

        FESolutionExecutor.log.info("Execute messages: {}", messages)
        val completion: Flowable<String> = completion.streamCompletion(messages)
        FESolutionExecutor.log.info("Execute completion: {}", completion)

        return Flowable.create({ emitter ->
            completion.subscribe({
                val answer = Answer(this.javaClass.name, it)
                emitter.onNext(answer)
            }, {
                emitter.onError(it)
            }, {
                emitter.onComplete()
            })
        }, BackpressureStrategy.BUFFER)
    }
}
