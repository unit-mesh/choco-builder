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
    val basePrompt = CodeSemanticWorkflow.EXECUTE.format()

    override fun execute(solution: ExplainQuery): Flowable<Answer> {
        variables.putQuery(solution)
        val query = embedding.embed(solution.englishQuery)
        val originQuery = embedding.embed(solution.originLanguageQuery)

        val list = store.findRelevant(query, 2)
        val originList = store.findRelevant(originQuery, 2)

        // remove duplicate in list
        val relevantDocuments = list.filter { !originList.contains(it) } + originList

        variables.putCode("", relevantDocuments.map { it.embedded.text })

        val messages = listOf(
            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, variables.compile(basePrompt)),
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
