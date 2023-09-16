package cc.unitmesh.cf.domains.semantic.flow

import cc.unitmesh.cf.core.dsl.Interpreter
import cc.unitmesh.cf.core.flow.SolutionExecutor
import cc.unitmesh.cf.core.flow.model.Answer
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.domains.semantic.model.ExplainQuery
import cc.unitmesh.cf.infrastructure.llms.embedding.SentenceTransformersEmbedding
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.store.EmbeddingStore
import io.reactivex.rxjava3.core.Flowable

class SemanticSolutionExecutor(
    val llmProvider: LlmProvider,
    val store: EmbeddingStore<Document>,
    val embedding: SentenceTransformersEmbedding
): SolutionExecutor<ExplainQuery> {
    override val interpreters: List<Interpreter> = listOf()

    override fun execute(solution: ExplainQuery): Flowable<Answer> {
        TODO("Not yet implemented")
    }
}
