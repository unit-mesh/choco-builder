package cc.unitmesh.cf.infrastructure.llms.completion

import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg

enum class TemperatureMode(val value: Double) {
    Creative(0.8),
    Balanced(0.5),
    Conservative(0.2),
    Default(0.0),
}

interface LlmProvider {
    var temperature: Double

    fun createCompletions(messages: List<LlmMsg.ChatMessage>): List<LlmMsg.ChatChoice>

    fun simpleCompletion(messages: List<LlmMsg.ChatMessage>): String

    fun setTemperatureMode(mode: TemperatureMode) {
        this.temperature = mode.value
    }

    class CompletionResultNotFoundException(val messages: List<LlmMsg.ChatMessage>) :
        RuntimeException("自动补齐无结果，提示词：${messages}")
}

