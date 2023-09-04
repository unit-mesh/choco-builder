package cc.unitmesh.cf.presentation

import cc.unitmesh.cf.core.prompt.PromptWithStage
import cc.unitmesh.cf.domains.SupportedDomains
import cc.unitmesh.cf.domains.frontend.context.FEWorkflow
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class ChatController {
    @PostMapping("/chat")
    fun chat(@RequestBody request: ChatRequest): SseEmitter {
        val emitter = SseEmitter()
        var output = "..."

        // 1. search by domains
        val workflow = when (request.domain) {
            SupportedDomains.Frontend -> {
                FEWorkflow()
            }

            SupportedDomains.Ktor -> TODO()
            SupportedDomains.SQL -> TODO()
            SupportedDomains.Custom -> TODO()
        }

        println(request.stage)
        // 2. searches by stage
        val prompt = workflow.prompts[request.stage]
            ?: throw RuntimeException("prompt not found!")

        println(prompt)

        // 3. search by id

        val response = MessageResponse(output, request.id, PromptWithStage.Stage.Analyze, true)

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

