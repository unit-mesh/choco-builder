package cc.unitmesh.cf.infrastructure.llms.completion

import cc.unitmesh.cf.infrastructure.llms.configuration.OpenAiConfiguration
import cc.unitmesh.cf.infrastructure.llms.model.ChatChoice
import cc.unitmesh.cf.infrastructure.llms.model.ChatMessage
import cc.unitmesh.cf.infrastructure.llms.model.ChatRole
import cc.unitmesh.cf.infrastructure.llms.model.FinishReason
import com.theokanning.openai.completion.chat.ChatCompletionChoice
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.service.OpenAiService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import java.time.Duration

@Component
@ConditionalOnProperty(prefix = "openai", name = ["api-key"])
class OpenAiCompletion(val config: OpenAiConfiguration) : CompletionProvider {
    var totalTokens = 0L;
    private val openai: OpenAiService by lazy { OpenAiService(config.apiKey, Duration.ZERO) }

    @Cacheable("completion")
    override fun createCompletions(messages: List<ChatMessage>): List<ChatChoice> {
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

    fun ChatMessage.toInternal() =
        com.theokanning.openai.completion.chat.ChatMessage(this.role.name.lowercase(), this.content, this.name)

    override fun prompt(promptText: String): String {
        TODO("Not yet implemented")
    }
}

private fun ChatCompletionChoice.toAbstract(): ChatChoice {
    return ChatChoice(
        index = index, message = message.toAbstract(), finishReason = FinishReason.fromValue(finishReason)
    )
}

private fun FinishReason.Companion.fromValue(finishReason: String): FinishReason {
    return enumValues<FinishReason>().find { it.value == finishReason }
        ?: throw EnumConstantNotPresentException(FinishReason::class.java, finishReason)
}

private fun com.theokanning.openai.completion.chat.ChatMessage.toAbstract(): ChatMessage {
    return ChatMessage(role = ChatRole.fromValue(role), content = content, name = name)
}

private fun ChatRole.Companion.fromValue(role: String): ChatRole {
    return enumValues<ChatRole>().find { it.value == role }
        ?: throw EnumConstantNotPresentException(ChatRole::class.java, role)
}