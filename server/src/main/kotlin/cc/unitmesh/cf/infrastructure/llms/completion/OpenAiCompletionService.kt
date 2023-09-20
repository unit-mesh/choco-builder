package cc.unitmesh.cf.infrastructure.llms.completion

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.infrastructure.llms.configuration.OpenAiConfiguration
import cc.unitmesh.openai.OpenAiProvider
import io.reactivex.rxjava3.core.Flowable
import org.springframework.stereotype.Component

@Component
class OpenAiCompletionService(final val config: OpenAiConfiguration) : LlmProvider {
    val provider = OpenAiProvider(config.apiKey, config.apiHost)
    override var temperature: Double = 0.0

    override fun completion(messages: List<LlmMsg.ChatMessage>): String {
       return provider.completion(messages)
    }

    override fun streamCompletion(messages: List<LlmMsg.ChatMessage>): Flowable<String> {
        return provider.streamCompletion(messages)
    }

}

