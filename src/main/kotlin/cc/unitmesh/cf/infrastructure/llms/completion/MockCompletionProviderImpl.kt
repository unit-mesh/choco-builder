package cc.unitmesh.cf.infrastructure.llms.completion

import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg
import org.springframework.stereotype.Component

class MockCompletionProvider : CompletionProvider {
    override fun createCompletions(messages: List<LlmMsg.ChatMessage>): List<LlmMsg.ChatChoice> {
        return listOf()
    }
}