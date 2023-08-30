package cc.unitmesh.cf.infrastructure.vector

import cc.unitmesh.cf.core.dsl.EmbeddingElement
import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding

interface Similarity {
    fun <T : EmbeddingElement> findNearest(
        data: List<T>,
        questionVector: Embedding,
        maxDistance: Double,
        maxResults: Int,
    ): List<T>
}
