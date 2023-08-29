package cc.unitmesh.cf.infrastructure.llms.embedding

import org.springframework.stereotype.Component

@Component
class SentenceTransformersEmbedding: EmbeddingProvider {
    override fun createEmbeddings(texts: List<String>): List<List<Double>> {
        return emptyList()
    }
}