package cc.unitmesh.cf.presentation.domain

import cc.unitmesh.cf.core.workflow.StageContext
import cc.unitmesh.cf.presentation.controller.ChatRequest
import cc.unitmesh.cf.presentation.controller.Message

data class ChatWebContext(
    val messages: List<Message>,
    val id: String,
    val stage: StageContext.Stage,
) {
    companion object {
        fun fromRequest(request: ChatRequest): ChatWebContext {
            return ChatWebContext(
                messages = request.messages,
                id = request.id,
                stage = request.stage,
            )
        }
    }
}