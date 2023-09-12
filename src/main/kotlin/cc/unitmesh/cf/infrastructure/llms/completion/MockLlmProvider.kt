package cc.unitmesh.cf.infrastructure.llms.completion

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.cf.core.llms.LlmProvider
import io.reactivex.rxjava3.core.Flowable

class MockLlmProvider : LlmProvider {
    override var temperature: Double = 0.0

    override fun createCompletions(messages: List<LlmMsg.ChatMessage>): List<LlmMsg.ChatChoice> {
        return listOf()
    }

    override fun simpleCompletion(messages: List<LlmMsg.ChatMessage>): String {
        return ""
    }

    override fun flowCompletion(messages: List<LlmMsg.ChatMessage>): Flowable<String> {
        TODO("Not yet implemented")
    }
}