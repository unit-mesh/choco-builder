package cc.unitmesh.cf.infrastructure.vector

import cc.unitmesh.cf.factory.dsl.BaseEmbedding
import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding

interface Similarity {
    fun <T : BaseEmbedding> findNearest(
        data: List<T>,
        questionVector: Embedding,
        maxDistance: Double,
        maxResults: Int,
    ): List<T>
}
