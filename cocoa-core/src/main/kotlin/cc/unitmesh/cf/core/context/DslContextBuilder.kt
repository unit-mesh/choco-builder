package cc.unitmesh.cf.core.context

import cc.unitmesh.cf.core.cache.CachableEmbedding
import cc.unitmesh.cf.core.dsl.InterpreterContext
import cc.unitmesh.nlp.similarity.Similarity

abstract class DslContextBuilder(
    private val similarity: Similarity,
    private val cachedEmbedding: CachableEmbedding
) {
    abstract fun buildFor(domain: InterpreterContext, question: String, chatHistories: String = ""): DslContext
}