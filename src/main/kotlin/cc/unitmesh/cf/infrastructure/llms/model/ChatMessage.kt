package cc.unitmesh.cf.infrastructure.llms.model

data class ChatMessage(
    val role: ChatRole,
    val content: String,
    val name: String? = null,
)

data class ChatChoice(
    val index: Int,
    val message: ChatMessage,
    val finishReason: FinishReason,
)

enum class FinishReason(val value: String) {
    Stopped("stop"),
    ContentFiltered("content_filter"),
    FunctionCall("function_call"),
    TokenLimitReached("length"),
    ;

    companion object
}
