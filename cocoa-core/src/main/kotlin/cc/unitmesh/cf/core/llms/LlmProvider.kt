package cc.unitmesh.cf.core.llms

import io.reactivex.rxjava3.core.Flowable

interface LlmProvider {
    var temperature: Double

    fun completion(messages: List<LlmMsg.ChatMessage>): String

    fun streamCompletion(messages: List<LlmMsg.ChatMessage>): Flowable<String>

    fun setTemperatureMode(mode: TemperatureMode) {
        this.temperature = mode.value
    }
}

