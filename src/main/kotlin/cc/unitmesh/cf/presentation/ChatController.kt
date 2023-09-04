package cc.unitmesh.cf.presentation

import cc.unitmesh.cf.core.prompt.PromptWithStage
import cc.unitmesh.cf.domains.SupportedDomains
import cc.unitmesh.cf.domains.frontend.FEWorkflow
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class ChatController(
    val feFlow: FEWorkflow,
) {
    @PostMapping("/chat")
    fun chat(@RequestBody request: ChatRequest): SseEmitter {
        val emitter = SseEmitter()
        val output = "..."

        // 1. search by domains
        val workflow = when (request.domain) {
            SupportedDomains.Frontend -> {
                feFlow
            }

            SupportedDomains.Ktor -> TODO()
            SupportedDomains.SQL -> TODO()
            SupportedDomains.Custom -> TODO()
        }

        // 2. searches by stage
        val prompt = workflow.prompts[request.stage]
            ?: throw RuntimeException("prompt not found!")

        // 3. execute stage with prompt
        println(prompt)

        // 4. return response
        val response = MessageResponse(output, request.id, prompt.stage, true)

        emitter.send(Json.encodeToString(response))
        emitter.complete()
        return emitter
    }
}

@Serializable
data class MessageResponse(val message: String, val id: String, val stage: PromptWithStage.Stage, val isDone: Boolean)

data class Message(val role: String, val content: String)

data class ChatRequest(
    val messages: List<Message>,
    val id: String,
    val stage: PromptWithStage.Stage,
    val domain: SupportedDomains,
    val previewToken: String?,
)

