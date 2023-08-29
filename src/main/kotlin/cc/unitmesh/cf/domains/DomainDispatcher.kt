package cc.unitmesh.cf.domains

import cc.unitmesh.cf.factory.process.DomainDetector
import cc.unitmesh.cf.infrastructure.cache.CachedEmbedding
import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding

class DomainDispatcher(
    private val cachedEmbedding: CachedEmbedding
): DomainDetector {
    override fun detect(question: String): List<String> {
        val question: Embedding = cachedEmbedding.createEmbedding(question)
        return listOf()
    }
}