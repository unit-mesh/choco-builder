package cc.unitmesh.cf.infrastructure.llms.completion

import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg

interface LlmProvider {
    fun createCompletions(messages: List<LlmMsg.ChatMessage>): List<LlmMsg.ChatChoice>

    fun simpleCompletion(messages: List<LlmMsg.ChatMessage>): String

    class CompletionResultNotFoundException(val messages: List<LlmMsg.ChatMessage>) :
        RuntimeException("自动补齐无结果，提示词：${messages}")
}

