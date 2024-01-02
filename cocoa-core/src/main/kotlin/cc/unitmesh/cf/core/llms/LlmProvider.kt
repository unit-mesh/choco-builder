package cc.unitmesh.cf.core.llms

import kotlinx.coroutines.flow.Flow

interface LlmProvider {
    var temperature: Double

    fun completion(messages: List<LlmMsg.ChatMessage>): String

    fun streamCompletion(messages: List<LlmMsg.ChatMessage>): Flow<String>

    fun setTemperatureMode(mode: TemperatureMode) {
        this.temperature = mode.value
    }
}

