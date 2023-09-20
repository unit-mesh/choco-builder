package cc.unitmesh.apply

import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.store.EmbeddingMatch

enum class EngineType {
    SentenceTransformers,
    TextEmbeddingAda,
}

class EmbeddingEngine(val engine: EngineType = EngineType.SentenceTransformers) {
    fun query(input: String) : List<EmbeddingMatch<Document>> {
        return listOf()
    }

    fun indexing(chunks: List<Document>): Boolean {
        return true
    }
}