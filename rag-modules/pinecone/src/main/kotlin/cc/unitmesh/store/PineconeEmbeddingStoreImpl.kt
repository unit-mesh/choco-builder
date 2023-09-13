package cc.unitmesh.store

import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.rag.document.TextSegment
import cc.unitmesh.rag.store.EmbeddingMatch
import cc.unitmesh.rag.store.EmbeddingStore

class PineconeEmbeddingStoreImpl : EmbeddingStore<TextSegment> {
    override fun add(embedding: Embedding): String {
        TODO("Not yet implemented")
    }

    override fun add(id: String, embedding: Embedding) {
        TODO("Not yet implemented")
    }

    override fun addAll(embeddings: List<Embedding>): List<String> {
        TODO("Not yet implemented")
    }

    override fun findRelevant(
        referenceEmbedding: Embedding,
        maxResults: Int,
        minScore: Double,
    ): List<EmbeddingMatch<TextSegment>> {
        TODO("Not yet implemented")
    }

    override fun addAll(embeddings: List<Embedding>, embedded: List<TextSegment>): List<String> {
        TODO("Not yet implemented")
    }

    override fun add(embedding: Embedding, embedded: TextSegment): String {
        TODO("Not yet implemented")
    }

}