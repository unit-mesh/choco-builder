package cc.unitmesh.cf.presentation.controller

import cc.unitmesh.cf.core.workflow.StageContext
import cc.unitmesh.cf.core.workflow.WorkflowResult
import cc.unitmesh.cf.domains.SupportedDomains
import cc.unitmesh.cf.domains.code.CodeInterpreterWorkflow
import cc.unitmesh.cf.domains.frontend.FEWorkflow
import cc.unitmesh.cf.presentation.domain.ChatWebContext
import cc.unitmesh.cf.presentation.ext.SseEmitterUtf8
import kotlinx.serialization.Serializable
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class ChatController(
    val feFlow: FEWorkflow,
    val codeFlow: CodeInterpreterWorkflow,
) {
    @PostMapping("/chat", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun chat(@RequestBody chat: ChatRequest): SseEmitter {
        val emitter = SseEmitterUtf8()

        // 1. search by domains
        val workflow = when (chat.domain) {
            SupportedDomains.Frontend -> {
                feFlow
            }

            SupportedDomains.Ktor -> TODO()
            SupportedDomains.SQL -> TODO()
            SupportedDomains.Custom -> TODO()
            SupportedDomains.CodeInterpreter -> {
                codeFlow
            }
        }

        // 2. searches by stage
        val prompt = workflow.prompts[chat.stage]
            ?: throw RuntimeException("prompt not found!")

        // 3. execute stage with prompt
        val chatWebContext = ChatWebContext.fromRequest(chat)
        val result = workflow.execute(prompt, chatWebContext)

        // 4. return response
        emitter.send(MessageResponse.from(chat.id, result))

        emitter.complete()
        return emitter
    }

    companion object {
        private val log = LoggerFactory.getLogger(ChatController::class.java)
    }
}

@Serializable
data class MessageResponse(
    val id: String,
    val result: WorkflowResult?,
    val created: Long = DateTime.now().millis,
    val messages: List<Message> = emptyList(),
) {
    companion object {
        fun from(id: String, result: WorkflowResult?): MessageResponse {
            val messages = listOf(Message("assistant", result?.responseMsg ?: ""))
            return MessageResponse(id, result, messages = messages)
        }
    }
}

@Serializable
data class Message(val role: String, val content: String)

data class ChatRequest(
    val messages: List<Message>,
    val id: String,
    val stage: StageContext.Stage,
    val domain: SupportedDomains,
    val previewToken: String?,
)

