package cc.unitmesh.openai

import cc.unitmesh.cf.core.llms.LlmMsg
import com.theokanning.openai.completion.chat.ChatCompletionChoice

private fun ChatCompletionChoice.toAbstract(): LlmMsg.ChatChoice {
    return LlmMsg.ChatChoice(
        index = index, message = message.toAbstract(), finishReason = LlmMsg.FinishReason.fromValue(finishReason)
    )
}

private fun LlmMsg.FinishReason.Companion.fromValue(finishReason: String): LlmMsg.FinishReason {
    return enumValues<LlmMsg.FinishReason>().find { it.value == finishReason }
        ?: throw EnumConstantNotPresentException(LlmMsg.FinishReason::class.java, finishReason)
}

private fun com.theokanning.openai.completion.chat.ChatMessage.toAbstract(): LlmMsg.ChatMessage {
    return LlmMsg.ChatMessage(role = LlmMsg.ChatRole.fromValue(role), content = content, name = name)
}

private fun LlmMsg.ChatRole.Companion.fromValue(role: String): LlmMsg.ChatRole {
    return enumValues<LlmMsg.ChatRole>().find { it.value == role }
        ?: throw EnumConstantNotPresentException(LlmMsg.ChatRole::class.java, role)
}