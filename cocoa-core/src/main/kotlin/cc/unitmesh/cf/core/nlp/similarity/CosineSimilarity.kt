package cc.unitmesh.cf.core.nlp.similarity

import cc.unitmesh.cf.core.llms.EmbeddingElement
import kotlin.math.sqrt

/**
 * Cosine similarity implementation.
 * use for Document, like LangChain, Spring AI, etc.
 */
class CosineSimilarity : Similarity {
    private fun cosineSimilarity(vectorX: List<Double>, vectorY: List<Double>): Double {
        require(vectorX.size == vectorY.size) { "Vectors lengths must be equal" }

        val dotProduct = dotProduct(vectorX, vectorY)
        val normX = norm(vectorX)
        val normY = norm(vectorY)

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

    override fun <T : EmbeddingElement> findNearest(
        data: List<T>,
        questionVector: List<Double>,
        maxDistance: Double,
        maxResults: Int,
    ): List<T> {
        return data
            .asSequence()
            .map { cosineSimilarity(it.embedding, questionVector) to it }
            .filter { it.first < maxDistance }
            .sortedBy { it.first }
            .take(maxResults)
            .map { (_, item) -> item }
            .toList()
    }
}
