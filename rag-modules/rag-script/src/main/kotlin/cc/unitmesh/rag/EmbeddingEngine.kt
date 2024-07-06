package cc.unitmesh.rag

import cc.unitmesh.cf.STEmbedding
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider
import cc.unitmesh.nlp.embedding.text.EnglishTextEmbeddingProvider

enum class EngineType {
    EnglishTextEmbedding,
    SentenceTransformers,
    TextEmbeddingAda,
}

class EmbeddingEngine(private val engine: EngineType = EngineType.SentenceTransformers) {
    var provider: EmbeddingProvider = when (engine) {
        EngineType.SentenceTransformers -> SentenceTransformersEmbedding()
        EngineType.EnglishTextEmbedding -> EnglishTextEmbeddingProvider()
        EngineType.TextEmbeddingAda -> TODO()
    }
}

class SentenceTransformersEmbedding : EmbeddingProvider {
    private val semantic = STEmbedding.create()
    override fun embed(texts: List<String>): List<Embedding> {
        return texts.map {
            semantic.embed(it).toList()
        }
    }
}