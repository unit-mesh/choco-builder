package cc.unitmesh.cf.presentation

import cc.unitmesh.cf.core.flow.model.ChatWebContext
import cc.unitmesh.cf.core.flow.model.Message
import cc.unitmesh.cf.core.flow.model.StageContext
import cc.unitmesh.cf.core.flow.model.WorkflowResult
import cc.unitmesh.cf.domains.SupportedDomains
import cc.unitmesh.cf.domains.frontend.FEWorkflow
import cc.unitmesh.cf.domains.interpreter.CodeInterpreterWorkflow
import cc.unitmesh.cf.domains.semantic.CodeSemanticWorkflow
import cc.unitmesh.cf.domains.spec.SpecWorkflow
import cc.unitmesh.cf.domains.testcase.TestcaseWorkflow
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.IOException


@Controller
class ChatController(
    val feFlow: FEWorkflow,
    val codeFlow: CodeInterpreterWorkflow,
    val testcaseFlow: TestcaseWorkflow,
    val specFlow: SpecWorkflow,
    val codeSemanticFlow: CodeSemanticWorkflow,
) {
    @PostMapping("/chat", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun chat(@RequestBody chat: ChatRequest): ResponseEntity<StreamingResponseBody> {
        // 1. search by domains
        val workflow = when (chat.domain) {
            SupportedDomains.Frontend -> feFlow
            SupportedDomains.CodeInterpreter -> codeFlow
            SupportedDomains.Testcase -> testcaseFlow
            SupportedDomains.Spec -> specFlow
            SupportedDomains.SQL -> TODO()
            SupportedDomains.Custom -> TODO()
            SupportedDomains.CodeSemanticSearch -> codeSemanticFlow
        }

        // 2. searches by stage
        var prompt = workflow.stages[chat.stage]
        if (prompt == null) {
            log.error("prompt not found! chat: {}", chat)
            prompt = workflow.stages.values.first()
        }

        // 3. execute stage with prompt
        val chatWebContext = chat.toContext()

        val out = StreamingResponseBody { outputStream ->
            runBlocking {
                val result = workflow.execute(prompt, chatWebContext)
                result
                    .observeOn(Schedulers.io())
                    .blockingForEach {
                        try {
                            val output = Json.encodeToString(MessageResponse.from(chat.id, it))
                            outputStream.write((output).toByteArray());
                            outputStream.flush()
                            outputStream.write("\n\n".toByteArray());
                            outputStream.flush()
                        } catch (e: IOException) {
                            log.error("{}", e)
                        }
                    }
            }
        }

        return ResponseEntity.ok().body(out)

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
    val isFlowable: Boolean = false,
    val messages: List<Message> = emptyList(),
) {
    companion object {
        fun from(id: String, result: WorkflowResult?): MessageResponse {
            val messages = listOf(Message("assistant", result?.responseMsg ?: ""))
            return MessageResponse(id, result, messages = messages, isFlowable = result?.isFlowable ?: false)
        }
    }
}

data class ChatRequest(
    val messages: List<Message>,
    val id: String,
    val stage: StageContext.Stage,
    val domain: SupportedDomains,
    val previewToken: String?,
) {
    fun toContext(): ChatWebContext {
        return ChatWebContext(
            messages = this.messages,
            id = this.id,
            stage = this.stage,
        )
    }
}
