package cc.unitmesh.rag

import cc.unitmesh.nlp.embedding.EmbeddingProvider
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.store.EmbeddingMatch
import cc.unitmesh.rag.store.EmbeddingStore
import cc.unitmesh.rag.store.InMemoryEmbeddingStore
import cc.unitmesh.rag.store.InMemoryEnglishTextStore
import cc.unitmesh.store.ElasticsearchStore

class Store(storeType: StoreType) {
    private var embedding: EmbeddingProvider? = null

    private val store: EmbeddingStore<Document> = when (storeType) {
        StoreType.Elasticsearch -> ElasticsearchStore()
        StoreType.Memory -> InMemoryEmbeddingStore()
        StoreType.MemoryEnglish -> InMemoryEnglishTextStore()
    }

    fun findRelevant(input: String): List<EmbeddingMatch<Document>> {
        val embedded = embedding!!.embed(input)
        return store.findRelevant(embedded, 20)
    }

    fun indexing(chunks: List<Document>): Boolean {
        val embeddings = chunks.map {
            embedding!!.embed(it.text)
        }

        store.addAll(embeddings, chunks)
        return true
    }

    fun updateEmbedding(value: EmbeddingProvider) {
        this.embedding = value
    }
}

enum class StoreType {
    Elasticsearch,
    Memory,
    MemoryEnglish,
}