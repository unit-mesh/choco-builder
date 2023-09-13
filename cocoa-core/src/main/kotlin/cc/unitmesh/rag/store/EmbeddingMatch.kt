package cc.unitmesh.rag.store

import cc.unitmesh.nlp.embedding.Embedding


data class EmbeddingMatch<Embedded>(
    val score: Double,
    val embeddingId: String,
    val embedding: Embedding,
    val embedded: Embedded,
) {

}