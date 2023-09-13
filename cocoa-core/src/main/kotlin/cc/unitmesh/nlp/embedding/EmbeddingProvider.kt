package cc.unitmesh.nlp.embedding

import cc.unitmesh.rag.document.Document

interface EmbeddingProvider {
    fun embed(texts: List<String>): List<Embedding>

    fun embed(text: String): Embedding {
        val results: List<Embedding> = embed(listOf(text))

        if (results.isNotEmpty()) {
            return results[0]
        }

        throw CreateEmbeddingFailedException(text)
    }

    fun embed(document: Document): Embedding {
        return embed(document.text)
    }

    class CreateEmbeddingFailedException(val text: String) : RuntimeException("创建嵌入模型失败，原文：$text")
}

