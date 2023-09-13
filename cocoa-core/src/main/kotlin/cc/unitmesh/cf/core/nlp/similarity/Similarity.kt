package cc.unitmesh.cf.core.nlp.similarity

import cc.unitmesh.cf.core.llms.Embedding
import cc.unitmesh.cf.core.llms.EmbeddingElement

interface Similarity {
    fun <T : EmbeddingElement> findNearest(
        data: List<T>,
        questionVector: Embedding,
        maxDistance: Double,
        maxResults: Int,
    ): List<T>

    fun similarityScore(set1: Embedding, set2: Embedding) : Double
}


class SimilarityData(val key: String, val similarity: Double)
