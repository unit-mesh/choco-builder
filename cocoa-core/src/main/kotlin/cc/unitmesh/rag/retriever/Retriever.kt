package cc.unitmesh.rag.retriever

import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.store.EmbeddingMatch

interface Retriever<Embedded> {
    fun retrieve(query: Embedding): List<EmbeddingMatch<Embedded>>
}