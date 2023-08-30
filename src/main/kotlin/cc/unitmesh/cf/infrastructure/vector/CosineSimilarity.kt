package cc.unitmesh.cf.infrastructure.vector

import cc.unitmesh.cf.core.dsl.EmbeddingElement
import kotlin.math.sqrt
import org.springframework.stereotype.Component

@Component
class CosineSimilarity : Similarity {
    internal fun cosineDistance(a: List<Double>, b: List<Double>): Double {
        require(a.size == b.size) { "Vectors must be of equal length" }

        var dotProduct = 0.0
        var normA = 0.0
        var normB = 0.0

        for (i in a.indices) {
            dotProduct += a[i] * b[i]
            normA += a[i] * a[i]
            normB += b[i] * b[i]
        }

        normA = sqrt(normA)
        normB = sqrt(normB)

        return if (normA * normB != 0.0) {
            1 - dotProduct / (normA * normB)
        } else {
            1.0
        }
    }

    override fun <T : EmbeddingElement> findNearest(
        data: List<T>,
        questionVector: List<Double>,
        maxDistance: Double,
        maxResults: Int,
    ): List<T> {
        return data
            .asSequence()
            .map { cosineDistance(it.embedding, questionVector) to it }
            .filter { it.first < maxDistance }
            .sortedBy { it.first }
            .take(maxResults)
            .map { (_, item) -> item }
            .toList()
    }
}
