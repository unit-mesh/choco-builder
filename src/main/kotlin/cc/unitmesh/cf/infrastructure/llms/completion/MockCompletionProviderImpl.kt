package cc.unitmesh.cf.infrastructure.llms.completion

import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg

class MockProvider : LlmProvider {
    override fun createCompletions(messages: List<LlmMsg.ChatMessage>): List<LlmMsg.ChatChoice> {
        return listOf()
    }

    override fun simpleCompletion(messages: List<LlmMsg.ChatMessage>): String {
        return ""
    }
}