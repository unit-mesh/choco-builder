package cc.unitmesh.cf.domains

import cc.unitmesh.cf.factory.process.Detector
import cc.unitmesh.cf.factory.process.DetectorPlaceholder
import cc.unitmesh.cf.infrastructure.cache.CachedEmbedding
import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding

class DomainDispatcher(
    private val cachedEmbedding: CachedEmbedding
) {
    fun dispatch(question: String): Detector {
        val question: Embedding = cachedEmbedding.createEmbedding(question)
        return DetectorPlaceholder()
    }
}