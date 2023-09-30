package cc.unitmesh.cf.core.llms

import io.reactivex.rxjava3.core.Flowable

class MockLlmProvider : LlmProvider {
    override var temperature: Double = 0.0

    override fun completion(messages: List<LlmMsg.ChatMessage>): String {
        return ""
    }

    override fun streamCompletion(messages: List<LlmMsg.ChatMessage>): Flowable<String> {
        return Flowable.empty()
    }
}