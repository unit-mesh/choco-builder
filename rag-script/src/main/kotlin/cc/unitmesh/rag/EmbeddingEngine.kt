package cc.unitmesh.rag

import cc.unitmesh.cf.STSemantic
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider

enum class EngineType {
    SentenceTransformers,
    TextEmbeddingAda,
}

class EmbeddingEngine(val engine: EngineType = EngineType.SentenceTransformers) {
    var provider: EmbeddingProvider = when (engine) {
        EngineType.SentenceTransformers -> SentenceTransformersEmbedding()
        EngineType.TextEmbeddingAda -> TODO()
    }
}

class SentenceTransformersEmbedding : EmbeddingProvider {
    val semantic = STSemantic.create()
    override fun embed(texts: List<String>): List<Embedding> {
        return texts.map {
            semantic.embed(it).toList()
        }
    }
}