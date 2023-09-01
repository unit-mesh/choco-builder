package cc.unitmesh.cf.infrastructure.llms.completion

import cc.unitmesh.cf.infrastructure.llms.configuration.OpenAiConfiguration
import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg
import com.theokanning.openai.client.OpenAiApi
import com.theokanning.openai.completion.chat.ChatCompletionChoice
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.service.OpenAiService
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.time.Duration

@Component
class OpenAiCompletion(val config: OpenAiConfiguration) : CompletionProvider {
    var totalTokens = 0L;
    private val openai: OpenAiService by lazy {
        // for proxy
        if (config.serverAddress != null) {
            val mapper = OpenAiService.defaultObjectMapper()
            val client = OpenAiService.defaultClient(config.apiKey, Duration.ZERO)

            val retrofit = Retrofit.Builder()
                .baseUrl(config.serverAddress!!)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            val api = retrofit.create(OpenAiApi::class.java)
            OpenAiService(api)
        } else {
            OpenAiService(config.apiKey, Duration.ZERO)
        }
    }

    @Cacheable("completion")
    override fun createCompletions(messages: List<LlmMsg.ChatMessage>): List<LlmMsg.ChatChoice> {
        val request =
            ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .temperature(0.0)
                .messages(messages.map { it.toInternal() }).build()

        val response = openai.createChatCompletion(request)
        totalTokens += response.usage.totalTokens
        if (response.choices[0].finishReason != "stop") {
            throw OpenAiCompletionException("Completion failed: ${response.choices[0].finishReason}")
        }
        return response.choices.map { it.toAbstract() }
    }

    fun LlmMsg.ChatMessage.toInternal() =
        com.theokanning.openai.completion.chat.ChatMessage(this.role.name.lowercase(), this.content, this.name)

    override fun prompt(promptText: String): String {
        TODO("Not yet implemented")
    }
}

private fun ChatCompletionChoice.toAbstract(): LlmMsg.ChatChoice {
    return LlmMsg.ChatChoice(
        index = index, message = message.toAbstract(), finishReason = LlmMsg.FinishReason.fromValue(finishReason)
    )
}

private fun LlmMsg.FinishReason.Companion.fromValue(finishReason: String): LlmMsg.FinishReason {
    return enumValues<LlmMsg.FinishReason>().find { it.value == finishReason }
        ?: throw EnumConstantNotPresentException(LlmMsg.FinishReason::class.java, finishReason)
}

private fun com.theokanning.openai.completion.chat.ChatMessage.toAbstract(): LlmMsg.ChatMessage {
    return LlmMsg.ChatMessage(role = LlmMsg.ChatRole.fromValue(role), content = content, name = name)
}

private fun LlmMsg.ChatRole.Companion.fromValue(role: String): LlmMsg.ChatRole {
    return enumValues<LlmMsg.ChatRole>().find { it.value == role }
        ?: throw EnumConstantNotPresentException(LlmMsg.ChatRole::class.java, role)
}