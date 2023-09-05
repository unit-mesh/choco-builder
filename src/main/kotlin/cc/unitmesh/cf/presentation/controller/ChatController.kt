package cc.unitmesh.cf.presentation.controller

import cc.unitmesh.cf.core.workflow.StageContext
import cc.unitmesh.cf.core.workflow.WorkflowResult
import cc.unitmesh.cf.domains.SupportedDomains
import cc.unitmesh.cf.domains.frontend.FEWorkflow
import cc.unitmesh.cf.presentation.domain.ChatWebContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class ChatController(val feFlow: FEWorkflow) {
    @PostMapping("/chat")
    fun chat(@RequestBody request: ChatRequest): SseEmitter {
        val emitter = SseEmitter()

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
        val chatWebContext = ChatWebContext.fromRequest(request)
        val result = feFlow.execute(prompt, chatWebContext)

        // 4. return response
        val response = MessageResponse(request.id, result)

        val encodeToString = Json.encodeToString(response)
        println(encodeToString)
        emitter.send(encodeToString)
        emitter.complete()
        return emitter
    }

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(ChatController::class.java)
    }
}

@Serializable
data class MessageResponse(val id: String, val result: WorkflowResult?)

data class Message(val role: String, val content: String)

data class ChatRequest(
    val messages: List<Message>,
    val id: String,
    val stage: StageContext.Stage,
    val domain: SupportedDomains,
    val previewToken: String?,
)

