package cc.unitmesh.cf.infrastructure.llms.completion

import cc.unitmesh.cf.core.llms.LlmMsg

class MockLlmProvider : LlmProvider {
    override var temperature: Double = 0.0

    override fun createCompletions(messages: List<LlmMsg.ChatMessage>): List<LlmMsg.ChatChoice> {
        return listOf()
    }

    override fun simpleCompletion(messages: List<LlmMsg.ChatMessage>): String {
        return ""
    }
}