package cc.unitmesh.nlp.embedding.text

import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider

class TextEmbeddingProvider : EmbeddingProvider {
    override fun embed(texts: List<String>): List<Embedding> {
        return texts.map { text ->
            val tokens = tokenize(text)
            tokens.map {
                it.toByteArray().map(Byte::toDouble)
            }.flatten()
        }
    }

    private fun tokenize(chunk: String): List<String> {
        return chunk.split(Regex("[^a-zA-Z0-9]")).filter { it.isNotBlank() }
    }
}