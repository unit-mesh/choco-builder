package cc.unitmesh.cf.infrastructure.cache

import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding
import cc.unitmesh.cf.infrastructure.llms.embedding.EmbeddingApi
import org.springframework.stereotype.Component

@Component
class CachedEmbedding(
    val embeddingApi: EmbeddingApi,
) {
    fun createEmbedding(question: String): Embedding {
        return embeddingApi.createEmbedding(question)
    }
}