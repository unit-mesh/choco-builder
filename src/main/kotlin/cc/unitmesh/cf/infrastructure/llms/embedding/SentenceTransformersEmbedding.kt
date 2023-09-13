package cc.unitmesh.cf.infrastructure.llms.embedding

import org.springframework.stereotype.Component
import cc.unitmesh.cf.STSemantic
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider

@Component
class SentenceTransformersEmbedding : EmbeddingProvider {
    val semantic = STSemantic.create()

    override fun embed(texts: List<String>): List<Embedding> {
        return texts.map {
            semantic.embed(it).toList()
        }
    }
}