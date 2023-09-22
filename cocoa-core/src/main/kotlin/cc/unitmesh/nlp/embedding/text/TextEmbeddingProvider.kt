package cc.unitmesh.nlp.embedding.text

import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider

class TextEmbeddingProvider : EmbeddingProvider {
    override fun embed(texts: List<String>): List<Embedding> {
        return texts.map {
            it.toByteArray().map(Byte::toDouble)
        }
    }
}