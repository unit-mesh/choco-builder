package cc.unitmesh.nlp.similarity

import cc.unitmesh.nlp.embedding.Embedding

/**
 * Jaccard similarity implementation.
 * use for Code, like GitHub Copilot, JetBrains AI Assistant
 */
class JaccardSimilarity : Similarity {
    override fun similarityScore(set1: Embedding, set2: Embedding): Double {
        val intersectionSize: Int = (set1 intersect set2).size
        val unionSize: Int = (set1 union set2).size
        return intersectionSize.toDouble() / unionSize.toDouble()
    }

    companion object {
        fun between(embedding: List<Double>, referenceEmbedding: List<Double>): Double {
            return JaccardSimilarity().similarityScore(embedding, referenceEmbedding)
        }
    }
}