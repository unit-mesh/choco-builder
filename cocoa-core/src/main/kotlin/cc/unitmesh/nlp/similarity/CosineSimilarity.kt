package cc.unitmesh.nlp.similarity

import cc.unitmesh.nlp.embedding.Embedding
import kotlin.math.sqrt

/**
 * Cosine similarity implementation.
 * use for Document, like LangChain, Spring AI, etc.
 */
class CosineSimilarity : Similarity {
    override fun similarityScore(vectorA: Embedding, vectorB: Embedding): Double {
        if (vectorA.size != vectorB.size) {
            throw IllegalArgumentException(
                "Length of vector a (${vectorA.size}) must be equal to the length of vector b (${vectorB.size})"
            )
        }

        var dotProduct = 0.0
        var normA = 0.0
        var normB = 0.0

        for (i in vectorA.indices) {
            dotProduct += (vectorA[i] * vectorB[i])
            normA += (vectorA[i] * vectorA[i])
            normB += (vectorB[i] * vectorB[i])
        }

        return dotProduct / (sqrt(normA) * sqrt(normB))
    }

    companion object {
        fun between(embedding: List<Double>, referenceEmbedding: List<Double>): Double {
            return CosineSimilarity().similarityScore(embedding, referenceEmbedding)
        }
    }
}
