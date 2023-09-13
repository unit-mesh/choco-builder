package cc.unitmesh.cf.core.nlp.similarity

import cc.unitmesh.cf.core.llms.EmbeddingElement
import kotlin.math.sqrt

/**
 * Cosine similarity implementation.
 * use for Document, like LangChain, Spring AI, etc.
 */
class CosineSimilarity : Similarity {
    override fun similarityScore(set1: List<Double>, set2: List<Double>): Double {
        require(set1.size == set2.size) { "Vectors lengths must be equal" }

        val dotProduct = dotProduct(set1, set2)
        val normX = norm(set1)
        val normY = norm(set2)

        require(!(normX == 0.0 || normY == 0.0)) { "Vectors cannot have zero norm" }
        return dotProduct / (sqrt(normX) * sqrt(normY))
    }

    private fun dotProduct(vectorX: List<Double>, vectorY: List<Double>): Double {
        require(vectorX.size == vectorY.size) { "Vectors lengths must be equal" }
        var result = 0.0
        for (i in vectorX.indices) {
            result += vectorX[i] * vectorY[i]
        }

        return result
    }

    private fun norm(vector: List<Double>): Double {
        return dotProduct(vector, vector)
    }
}
