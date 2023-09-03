package cc.unitmesh.cf.infrastructure.llms.completion

import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface LlmProvider {
    fun createCompletions(messages: List<LlmMsg.ChatMessage>): List<LlmMsg.ChatChoice>

    fun createCompletion(messages: List<LlmMsg.ChatMessage>): LlmMsg.ChatMessage {
        val result = createCompletions(messages)
        if (result.isEmpty()) {
            throw CompletionResultNotFoundException(messages)
        }
        if (result[0].finishReason != LlmMsg.FinishReason.Stopped) {
            throw CompletionFailedException(result[0].finishReason)
        }
        return result[0].message
    }

    class CompletionFailedException(val finishReason: LlmMsg.FinishReason) :
        RuntimeException("自动补齐异常结束，原因：$finishReason")

    class CompletionResultNotFoundException(val messages: List<LlmMsg.ChatMessage>) :
        RuntimeException("自动补齐无结果，提示词：${messages}")


    fun prompt(promptText: String): String = ""

    fun stream(promptText: String, systemPrompt: String): Flow<String> {
        return callbackFlow {
            val prompt = prompt(promptText)
            trySend(prompt)

            awaitClose()
        }
    }
}

