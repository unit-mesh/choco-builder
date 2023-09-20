package cc.unitmesh.apply

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.openai.OpenAiProvider
import io.reactivex.rxjava3.core.Flowable

/**
 * for create LlmProvider
 */
class Connection(val connectionType: ConnectionType, val apiKey: String, val apiHost: String? = null) : LlmProvider {
    val provider: LlmProvider = when (connectionType) {
        ConnectionType.OpenAI -> OpenAiProvider(apiKey, apiHost)
    }

    constructor(openAI: ConnectionType) : this(
        openAI,
        "",
        ""
    )

    override var temperature: Double = 0.0

    fun completion(msg: String): String {
        return provider.completion(listOf(LlmMsg.ChatMessage(LlmMsg.ChatRole.User, msg)))
    }

    override fun completion(messages: List<LlmMsg.ChatMessage>): String {
        return provider.completion(messages)
    }

    override fun streamCompletion(messages: List<LlmMsg.ChatMessage>): Flowable<String> {
        return provider.streamCompletion(messages)
    }

    fun completion(function: () -> String): String {
        return completion(function())
    }

    fun streamCompletion(function: () -> String): Flowable<String> {
        return provider.streamCompletion(listOf(LlmMsg.ChatMessage(LlmMsg.ChatRole.User, function())))
    }
}

enum class ConnectionType {
    OpenAI
}