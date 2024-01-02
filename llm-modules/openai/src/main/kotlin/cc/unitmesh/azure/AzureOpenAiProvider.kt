package cc.unitmesh.azure

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.cf.core.llms.LlmProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.theokanning.openai.completion.chat.ChatCompletionResult
import com.theokanning.openai.service.SSE
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.time.Duration

class AzureOpenAiProvider(var apiKey: String, var apiHost: String) : LlmProvider {
    override var temperature = 0.0
    private val timeout = Duration.ofSeconds(600)
    private var client = OkHttpClient().newBuilder().readTimeout(timeout).build()

    fun LlmMsg.ChatMessage.toInternal() =
        com.theokanning.openai.completion.chat.ChatMessage(this.role.name.lowercase(), this.content)

    override fun completion(messages: List<LlmMsg.ChatMessage>): String {
        val simpleOpenAIFormats = messages.map {
            SimpleOpenAIFormat.fromChatMessage(it.toInternal())
        }
        val requestText = Json.encodeToString<SimpleOpenAIBody>(
            SimpleOpenAIBody(
                simpleOpenAIFormats,
                0.0,
                false
            )
        )

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            requestText
        )

        val builder = Request.Builder()
        val request = builder
            .url(apiHost)
            .post(body)
            .build()
        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            return ""
        }

        val completion: ChatCompletionResult =
            ObjectMapper().readValue(response.body?.string(), ChatCompletionResult::class.java)

        return completion.choices[0].message.content
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun streamCompletion(messages: List<LlmMsg.ChatMessage>): Flow<String> {
        val simpleOpenAIFormats = messages.map {
            SimpleOpenAIFormat.fromChatMessage(it.toInternal())
        }
        val requestText = Json.encodeToString<SimpleOpenAIBody>(
            SimpleOpenAIBody(
                simpleOpenAIFormats,
                0.0,
                false
            )
        )

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            requestText
        )

        val builder = Request.Builder()
        val request = builder
            .url(apiHost!!)
            .post(body)
            .build()

        val call = client.newCall(request)
        val emitDone = false

        val sseFlowable = Flowable
            .create({ emitter: FlowableEmitter<SSE> ->
                call.enqueue(ResponseBodyCallback(emitter, emitDone))
            }, BackpressureStrategy.BUFFER)

        var output = ""

        return callbackFlow {
            sseFlowable
                .doOnError(Throwable::printStackTrace)
                .blockingForEach { sse ->
                    val result: ChatCompletionResult =
                        ObjectMapper().readValue(sse.data, ChatCompletionResult::class.java)
                    val completion = result.choices[0].message
                    if (completion != null && completion.content != null) {
                        output += completion.content
                        trySend(completion.content)
                    }
                }

            close()
        }
    }

}