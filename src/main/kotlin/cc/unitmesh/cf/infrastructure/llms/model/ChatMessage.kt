package cc.unitmesh.cf.infrastructure.llms.model

data class ChatMessage(
    val role: ChatRole,
    val content: String,
    val name: String? = null,
)