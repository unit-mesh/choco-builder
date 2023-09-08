package cc.unitmesh.cf.core.llms

class LlmMsg {
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

    enum class ChatRole(val value: String) {
        System("system"),
        User("user"),
        Assistant("assistant"),
        Function("function"),
        ;

        companion object
    }
}
