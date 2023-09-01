package cc.unitmesh.cf.core.context

import cc.unitmesh.cf.core.dsl.InterpreterContext
import cc.unitmesh.cf.infrastructure.cache.CachedEmbedding
import cc.unitmesh.cf.infrastructure.similarity.Similarity

abstract class DslContextBuilder(
    private val similarity: Similarity,
    private val cachedEmbedding: CachedEmbedding
) {
    abstract fun buildFor(domain: InterpreterContext, question: String, chatHistories: String = ""): DslContext
}