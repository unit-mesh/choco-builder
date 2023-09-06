package cc.unitmesh.code.messaging

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    var id: Int = -1,
    var resultValue: String,
    var displayValue: String,
    var className: String = "",
    var msgType: MessageType = MessageType.NONE,
    var content: MessageContent? = null,
)
