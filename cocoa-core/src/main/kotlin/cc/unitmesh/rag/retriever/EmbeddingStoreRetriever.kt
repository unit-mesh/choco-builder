package cc.unitmesh.rag.retriever

import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.store.EmbeddingMatch
import cc.unitmesh.rag.store.EmbeddingStore
import java.util.*


class EmbeddingStoreRetriever : Retriever<Document> {
    private var vectorStore: EmbeddingStore<Document>
    private var k: Int
    private var threshold = Optional.empty<Double>()

    constructor(vectorStore: EmbeddingStore<Document>, k: Int = 4) {
        this.vectorStore = vectorStore
        this.k = k
    }

    constructor(vectorStore: EmbeddingStore<Document>, k: Int, threshold: Double) {
        this.vectorStore = vectorStore
        this.k = k
        this.threshold = Optional.of(threshold)
    }

    fun getEmbeddingStore(): EmbeddingStore<Document> {
        return vectorStore
    }

    override fun retrieve(query: Embedding): List<EmbeddingMatch<Document>> {
        return if (threshold.isPresent) {
            vectorStore.findRelevant(query, k, threshold.get())
        } else {
            vectorStore.findRelevant(query, k)
        }
    }
}

