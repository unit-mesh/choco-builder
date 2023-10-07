package cc.unitmesh.rag

import cc.unitmesh.nlp.embedding.EmbeddingProvider
import cc.unitmesh.nlp.embedding.text.EnglishTextEmbeddingProvider
import cc.unitmesh.rag.base.RagScript
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.store.EmbeddingMatch
import cc.unitmesh.rag.store.EmbeddingStore
import cc.unitmesh.rag.store.InMemoryEmbeddingStore
import cc.unitmesh.rag.store.InMemoryEnglishTextStore
import cc.unitmesh.store.ElasticsearchStore

/**
 * Store 用于存储文档的向量数据。
 * 支持 Elasticsearch、Milvus、InMemory 等存储引擎。
 */
class Store(storeType: StoreType) {
    private var embedding: EmbeddingProvider? = EnglishTextEmbeddingProvider()

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