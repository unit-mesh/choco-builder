package cc.unitmesh.cf.infrastructure.llms.completion.model

import com.theokanning.openai.completion.chat.ChatMessage
import kotlinx.serialization.Serializable

@Serializable
data class SimpleOpenAIFormat(val role: String, val content: String) {
    companion object {
        fun fromChatMessage(message: ChatMessage): SimpleOpenAIFormat {
            return SimpleOpenAIFormat(message.role, message.content)
        }
    }
}

@Serializable
data class SimpleOpenAIBody(val messages: List<SimpleOpenAIFormat>, val temperature: Double, val stream: Boolean)
