package cc.unitmesh.cf.core.llms

import io.reactivex.rxjava3.core.Flowable

class MockLlmProvider(val response: String = "") : LlmProvider {
    override var temperature: Double = 0.0

    override fun completion(messages: List<LlmMsg.ChatMessage>): String {
        return response
    }

    override fun streamCompletion(messages: List<LlmMsg.ChatMessage>): Flowable<String> {
        return Flowable.just(response)
    }
}