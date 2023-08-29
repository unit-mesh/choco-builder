package cc.unitmesh.cf.factory.context

import cc.unitmesh.cf.factory.dsl.InterpreterContext
import cc.unitmesh.cf.infrastructure.cache.CachedEmbedding
import cc.unitmesh.cf.infrastructure.vector.Similarity

abstract class DslContextBuilder(
    private val similarity: Similarity,
    private val cachedEmbedding: CachedEmbedding
) {
    abstract fun buildFor(domain: InterpreterContext, question: String, chatHistories: String = ""): DslContext
}