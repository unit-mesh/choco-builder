package cc.unitmesh.openai

import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider
import com.theokanning.openai.embedding.EmbeddingRequest
import com.theokanning.openai.service.OpenAiService
import java.time.Duration

class OpenAiEmbeddingProvider(var apiKey: String) : EmbeddingProvider {
    private val timeout = Duration.ofSeconds(600)
    var totalTokens = 0L

    private val openai: OpenAiService by lazy { OpenAiService(apiKey, timeout) }

    override fun embed(texts: List<String>): List<Embedding> {
        val request = EmbeddingRequest.builder().model("text-embedding-ada-002").input(texts).build();
        val response = openai.createEmbeddings(request)
        totalTokens += response.usage.totalTokens
        return response.data.map { it.embedding }
    }
}