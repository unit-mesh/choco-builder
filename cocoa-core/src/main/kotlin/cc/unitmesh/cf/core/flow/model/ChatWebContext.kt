package cc.unitmesh.cf.core.flow.model

import kotlinx.serialization.Serializable

data class ChatWebContext(
    var messages: List<Message>,
    val id: String,
    val stage: StageContext.Stage,
) {
}

@Serializable
data class Message(val role: String, val content: String)