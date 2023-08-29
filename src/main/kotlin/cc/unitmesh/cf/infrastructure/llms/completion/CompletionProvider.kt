package cc.unitmesh.cf.infrastructure.llms.completion

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface CompletionProvider {
    fun prompt(promptText: String): String

    fun stream(promptText: String, systemPrompt: String): Flow<String> {
        return callbackFlow {
            val prompt = prompt(promptText)
            trySend(prompt)

            awaitClose()
        }
    }
}

