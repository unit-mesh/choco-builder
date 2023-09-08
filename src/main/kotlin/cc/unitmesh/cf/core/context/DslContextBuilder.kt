package cc.unitmesh.cf.core.context

import cc.unitmesh.cf.core.dsl.InterpreterContext
import cc.unitmesh.cf.infrastructure.cache.CachedEmbeddingService
import cc.unitmesh.cf.core.nlp.similarity.Similarity

abstract class DslContextBuilder(
    private val similarity: Similarity,
    private val cachedEmbedding: CachedEmbeddingService
) {
    abstract fun buildFor(domain: InterpreterContext, question: String, chatHistories: String = ""): DslContext
}