package cc.unitmesh.cf.infrastructure.llms.embedding

import org.springframework.stereotype.Component
import cc.unitmesh.cf.STSemantic

@Component
class SentenceTransformersEmbedding : EmbeddingProvider {
    val semantic = STSemantic.create()
    override fun createEmbeddings(texts: List<String>): List<Embedding> {
        return texts.map {
            semantic.embed(it).toList()
        }
    }
}