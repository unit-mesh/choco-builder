package cc.unitmesh.cf.infrastructure.cache

import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding
import cc.unitmesh.cf.infrastructure.llms.embedding.EmbeddingProvider
import cc.unitmesh.cf.infrastructure.repository.EmbeddingCacheRepository
import org.springframework.stereotype.Component

@Component
class CachedEmbedding(
    val embeddingProvider: EmbeddingProvider,
    val cacheRepository: EmbeddingCacheRepository,
) {
    fun createEmbedding(text: String): Embedding {
        val entry = cacheRepository.findByText(text) ?: cacheRepository.save(
            EmbeddingCache(text = text, embedding = embeddingProvider.createEmbedding(text))
        )
        return entry.embedding
    }
}