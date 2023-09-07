package cc.unitmesh.cf.infrastructure.llms.embedding

// I don't know why, but sometimes the following import is not added automatically.
public typealias Embedding = List<Double>

interface EmbeddingProvider {
    fun createEmbeddings(texts: List<String>): List<Embedding>

    fun createEmbedding(text: String): Embedding {
        val results: List<Embedding> = createEmbeddings(listOf(text))
        if (results.isNotEmpty()) {
            return results[0]
        }

        throw CreateEmbeddingFailedException(text)
    }

    class CreateEmbeddingFailedException(val text: String) : RuntimeException("创建嵌入模型失败，原文：$text")
}

