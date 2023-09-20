package cc.unitmesh.apply

import cc.unitmesh.cf.STSemantic
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider
import cc.unitmesh.nlp.embedding.EncodingTokenizer
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.store.EmbeddingMatch

enum class EngineType {
    SentenceTransformers,
    TextEmbeddingAda,
}

class EmbeddingEngine(val engine: EngineType = EngineType.SentenceTransformers) {
    var embeddingStore: EmbeddingProvider = when (engine) {
        EngineType.SentenceTransformers -> SentenceTransformersEmbedding()
        EngineType.TextEmbeddingAda -> TODO()
    }

    fun embedded(input: String): Embedding {
        return embeddingStore.embed(input)
    }
}

class SentenceTransformersEmbedding : EmbeddingProvider {
    val semantic = STSemantic.create()
    val tokenizer = semantic.getTokenizer()

    override fun embed(texts: List<String>): List<Embedding> {
        return texts.map {
            semantic.embed(it).toList()
        }
    }
}