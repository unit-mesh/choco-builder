package cc.unitmesh.cf.domains

import cc.unitmesh.cf.factory.process.DomainDetector
import cc.unitmesh.cf.factory.process.DomainDetectorPlaceholder
import cc.unitmesh.cf.infrastructure.cache.CachedEmbedding
import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding

class DomainDispatcher(
    private val cachedEmbedding: CachedEmbedding
) {
    fun dispatch(question: String): DomainDetector {
        val question: Embedding = cachedEmbedding.createEmbedding(question)
        return DomainDetectorPlaceholder()
    }
}