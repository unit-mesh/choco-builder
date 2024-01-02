package cc.unitmesh.cf.core.llms

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MockLlmProvider(val response: String = "") : LlmProvider {
    override var temperature: Double = 0.0

    override fun completion(messages: List<LlmMsg.ChatMessage>): String {
        return response
    }

    override fun streamCompletion(messages: List<LlmMsg.ChatMessage>): Flow<String> {
        return callbackFlow {
            trySend(response)

            close()
        }
    }
}