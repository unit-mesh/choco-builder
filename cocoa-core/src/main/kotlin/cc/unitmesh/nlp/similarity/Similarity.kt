package cc.unitmesh.nlp.similarity

import cc.unitmesh.nlp.embedding.Embedding

interface Similarity {
    fun similarityScore(set1: Embedding, set2: Embedding) : Double
}

const val EMBEDDING_DIM = 384
fun meanPool(embeddings: List<Embedding>): Embedding {
    val len = embeddings.size.toFloat()
    val result = MutableList(EMBEDDING_DIM) { 0.0 }
    for (embedding in embeddings) {
        for ((i, v) in embedding.withIndex()) {
            result[i] += v
        }
    }
    result.forEach { v -> v / len }
    return result
}