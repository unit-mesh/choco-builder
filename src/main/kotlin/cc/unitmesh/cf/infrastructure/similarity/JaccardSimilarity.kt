package cc.unitmesh.cf.infrastructure.similarity

import cc.unitmesh.cf.core.dsl.EmbeddingElement
import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding

// todo: test in real world
class JaccardSimilarity : Similarity {
    override fun <T : EmbeddingElement> findNearest(
        data: List<T>,
        questionVector: Embedding,
        maxDistance: Double,
        maxResults: Int,
    ): List<T> {
        return data
            .asSequence()
            .map { similarityScore(it.embedding, questionVector) to it }
            .filter { it.first < maxDistance }
            .sortedBy { it.first }
            .take(maxResults)
            .map { (_, item) -> item }
            .toList()
    }

    fun similarityScore(set1: Embedding, set2: Embedding): Double {
        val intersectionSize: Int = (set1 intersect set2).size
        val unionSize: Int = (set1 union set2).size
        return intersectionSize.toDouble() / unionSize.toDouble()
    }
}