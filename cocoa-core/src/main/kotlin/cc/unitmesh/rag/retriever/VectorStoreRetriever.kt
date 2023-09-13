package cc.unitmesh.rag.retriever

import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.store.VectorStore
import java.util.*


class VectorStoreRetriever : Retriever {
    private var vectorStore: VectorStore
    private var k: Int
    private var threshold = Optional.empty<Double>()

    constructor(vectorStore: VectorStore, k: Int = 4) {
        this.vectorStore = vectorStore
        this.k = k
    }

    constructor(vectorStore: VectorStore, k: Int, threshold: Double) {
        this.vectorStore = vectorStore
        this.k = k
        this.threshold = Optional.of(threshold)
    }

    fun getVectorStore(): VectorStore {
        return vectorStore
    }

    override fun retrieve(query: String): List<Document> {
        return if (threshold.isPresent) {
            vectorStore.findRelevant(query, k, threshold.get())
        } else {
            vectorStore.findRelevant(query, k)
        }
    }
}

