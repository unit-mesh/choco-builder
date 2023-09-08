package cc.unitmesh.cf.infrastructure.similarity

import cc.unitmesh.cf.core.dsl.EmbeddingElement
import cc.unitmesh.cf.core.llms.Embedding

interface Similarity {
    fun <T : EmbeddingElement> findNearest(
        data: List<T>,
        questionVector: Embedding,
        maxDistance: Double,
        maxResults: Int,
    ): List<T>
}
