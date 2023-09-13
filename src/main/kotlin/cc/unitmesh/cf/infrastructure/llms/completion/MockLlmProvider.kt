package cc.unitmesh.cf.infrastructure.llms.completion

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.cf.core.llms.LlmProvider
import io.reactivex.rxjava3.core.Flowable

class MockLlmProvider : LlmProvider {
    override var temperature: Double = 0.0

    override fun completion(messages: List<LlmMsg.ChatMessage>): String {
        return ""
    }

    override fun streamCompletion(messages: List<LlmMsg.ChatMessage>): Flowable<String> {
        TODO("Not yet implemented")
    }
}