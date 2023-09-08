package cc.unitmesh.cf.core.llms

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

