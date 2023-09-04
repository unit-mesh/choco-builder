package cc.unitmesh.cf.presentation

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@CrossOrigin(maxAge = 3600)
@RestController
class PostController {

    @CrossOrigin
    @PostMapping("/chat")
    fun chat(@RequestBody messageRequest: MessageRequest): SseEmitter {
        val emitter = SseEmitter()

        val output = "Hello World!"
        emitter.send(output)
        emitter.complete()
        return emitter
    }
}

data class Message(val role: String, val content: String)

data class MessageRequest(
    val messages: List<Message>,
    val id: String,
    val domain: String,
    val previewToken: String?,
)