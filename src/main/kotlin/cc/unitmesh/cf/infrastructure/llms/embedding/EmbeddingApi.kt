package cc.unitmesh.cf.infrastructure.llms.embedding

typealias Embedding = List<Double>

interface EmbeddingApi {
    fun createEmbeddings(texts: List<String>): List<List<Double>>

    fun createEmbedding(text: String): List<Double> {
        val results = createEmbeddings(listOf(text))
        if (results.isEmpty()) {
            throw CreateEmbeddingFailedException(text)
        }
        return results[0]
    }
}

class CreateEmbeddingFailedException(val text: String) : RuntimeException("创建嵌入模型失败，原文：$text")

