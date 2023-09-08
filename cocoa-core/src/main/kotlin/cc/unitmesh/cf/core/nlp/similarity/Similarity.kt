package cc.unitmesh.cf.core.nlp.similarity

import cc.unitmesh.cf.core.llms.EmbeddingElement
import cc.unitmesh.cf.core.llms.Embedding

interface Similarity {
    fun <T : EmbeddingElement> findNearest(
        data: List<T>,
        questionVector: Embedding,
        maxDistance: Double,
        maxResults: Int,
    ): List<T>
}
