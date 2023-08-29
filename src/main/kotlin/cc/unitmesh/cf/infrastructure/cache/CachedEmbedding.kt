package cc.unitmesh.cf.infrastructure.cache

import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding
import cc.unitmesh.cf.infrastructure.llms.embedding.EmbeddingApi
import cc.unitmesh.cf.infrastructure.repository.EmbeddingCacheRepository
import org.springframework.stereotype.Component

@Component
class CachedEmbedding(
    val embeddingApi: EmbeddingApi,
    val cacheRepository: EmbeddingCacheRepository,
) {
    fun createEmbedding(text: String): Embedding {
        val entry = cacheRepository.findByText(text) ?: cacheRepository.save(
            EmbeddingCache(text = text, embedding = embeddingApi.createEmbedding(text))
        )
        return entry.embedding
    }
}