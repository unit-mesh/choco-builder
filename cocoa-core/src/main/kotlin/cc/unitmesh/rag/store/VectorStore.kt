package cc.unitmesh.rag.store

import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.similarity.Similarity
import java.util.*


interface VectorStore<Embedded> {
    val similarity: Similarity
    fun addAll(documents: List<Embedded>)
    fun add(document: Embedded)

    fun add(id: String, embedding: Embedding)

    fun findRelevant(referenceEmbedding: Embedding, maxResults: Int, minSimilarity: Double): List<Embedded>
}
