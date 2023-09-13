package cc.unitmesh.cf.infrastructure.llms.embedding

import cc.unitmesh.cf.core.llms.Embedding
import cc.unitmesh.cf.core.llms.EmbeddingProvider
import cc.unitmesh.cf.infrastructure.llms.configuration.OpenAiConfiguration
import com.theokanning.openai.embedding.EmbeddingRequest
import com.theokanning.openai.service.OpenAiService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.Duration

@Component
@Profile("openai")
@ConditionalOnProperty(prefix = "openai", name = ["api-key"])
class OpenAiEmbeddingProvider(val config: OpenAiConfiguration) : EmbeddingProvider {
    private val timeout = Duration.ofSeconds(600)
    var totalTokens = 0L

    private val openai: OpenAiService by lazy { OpenAiService(config.apiKey, timeout) }

    @Cacheable("embedding")
    override fun embed(texts: List<String>): List<Embedding> {
        val request = EmbeddingRequest.builder().model("text-embedding-ada-002").input(texts).build();
        val response = openai.createEmbeddings(request)
        totalTokens += response.usage.totalTokens
        return response.data.map { it.embedding }
    }
}
