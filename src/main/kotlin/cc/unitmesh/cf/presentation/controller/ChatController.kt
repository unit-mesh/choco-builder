package cc.unitmesh.cf.presentation.controller

import cc.unitmesh.cf.core.workflow.StageContext
import cc.unitmesh.cf.core.workflow.WorkflowResult
import cc.unitmesh.cf.domains.SupportedDomains
import cc.unitmesh.cf.domains.frontend.FEWorkflow
import cc.unitmesh.cf.presentation.domain.ChatWebContext
import cc.unitmesh.cf.presentation.ext.SseEmitterUtf8
import com.azure.ai.openai.models.ChatRole
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class ChatController(val feFlow: FEWorkflow) {
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
        }

        // 2. searches by stage
        val prompt = workflow.prompts[chat.stage]
            ?: throw RuntimeException("prompt not found!")

        // 3. execute stage with prompt
        val chatWebContext = ChatWebContext.fromRequest(chat)
        val result = feFlow.execute(prompt, chatWebContext)

        // 4. return response
        val encodeToString = Json.encodeToString(MessageResponse.from(chat.id, result))

        emitter.send(encodeToString)
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
    val `object`: String,
    val created: Long = DateTime.now().millis,
    val model: String = "gpt-3.5-turbo",
    val choices: List<MsgChoice> = emptyList(),
) {

    @Serializable
    data class MsgChoice(val index: Int, val delta: MsgDelta, val finish_reason: String = "stop")

    @Serializable
    data class MsgDelta(val role: String, val content: String)

    companion object {
        fun from(id: String, result: WorkflowResult?): MessageResponse {
            val delta = MsgDelta("assistant", result?.responseMsg ?: "")
            val choices = listOf(MsgChoice(0, delta))
            return MessageResponse(id, Json.encodeToString(result), choices = choices)
        }
    }
}

data class Message(val role: String, val content: String)

data class ChatRequest(
    val messages: List<Message>,
    val id: String,
    val stage: StageContext.Stage,
    val domain: SupportedDomains,
    val previewToken: String?,
)

