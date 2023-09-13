package cc.unitmesh.cf.domains.spec

import cc.unitmesh.nlp.embedding.EmbeddingProvider
import org.springframework.stereotype.Component

@Component
class SpecRelevantSearch(val embeddingProvider: EmbeddingProvider) {
    fun search(query: String): List<String> {
        return listOf()
    }
}