package cc.unitmesh.cf.presentation

import cc.unitmesh.cf.core.prompt.PromptWithStage
import cc.unitmesh.cf.domains.SupportedDomains
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class PostController {

    @PostMapping("/chat")
    fun chat(@RequestBody messageRequest: MessageRequest): SseEmitter {
        val emitter = SseEmitter()

        val output = "Hello World!"
        val response = MessageResponse(output, messageRequest.id, PromptWithStage.Stage.Analyze, true)

        emitter.send(Json.encodeToString(response))
        emitter.complete()
        return emitter
    }
}

@Serializable
data class MessageResponse(val message: String, val id: String, val stage: PromptWithStage.Stage, val isDone: Boolean)

data class Message(val role: String, val content: String)

data class MessageRequest(
    val messages: List<Message>,
    val id: String,
    val stage: PromptWithStage.Stage,
    val domain: SupportedDomains,
    val previewToken: String?,
)

