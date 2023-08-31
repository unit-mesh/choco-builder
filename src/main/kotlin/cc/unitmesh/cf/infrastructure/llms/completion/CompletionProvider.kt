package cc.unitmesh.cf.infrastructure.llms.completion

import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface CompletionProvider {
    fun createCompletions(messages: List<LlmMsg.ChatMessage>): List<LlmMsg.ChatChoice>

    fun prompt(promptText: String): String = ""

    fun stream(promptText: String, systemPrompt: String): Flow<String> {
        return callbackFlow {
            val prompt = prompt(promptText)
            trySend(prompt)

            awaitClose()
        }
    }
}

