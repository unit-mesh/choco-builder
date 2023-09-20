package cc.unitmesh.cf.infrastructure.llms.embedding

import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider
import cc.unitmesh.cf.infrastructure.llms.configuration.OpenAiConfiguration
import cc.unitmesh.openai.OpenAiEmbeddingProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("openai")
@ConditionalOnProperty(prefix = "openai", name = ["api-key"])
class OpenAiEmbeddingService(val config: OpenAiConfiguration)  : EmbeddingProvider {
    val providerWrapper = OpenAiEmbeddingProvider(config.apiKey)

    @Cacheable("embedding")
    override fun embed(texts: List<String>): List<Embedding> {
        return providerWrapper.embed(texts)
    }
}

