package cc.unitmesh.openai

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.cf.core.llms.LlmProvider
import com.theokanning.openai.client.OpenAiApi
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.service.OpenAiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.time.Duration

class OpenAiProvider(var apiKey: String, var apiHost: String? = null) : LlmProvider {
    override var temperature = 0.0

    private val openai: OpenAiService by lazy {
        // for proxy
        if (apiHost != null) {
            val mapper = OpenAiService.defaultObjectMapper()
            val client = OpenAiService.defaultClient(apiKey, Duration.ZERO)

            val host = apiHost!!.removeSurrounding("\"")

            val retrofit = Retrofit.Builder()
                .baseUrl(host)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            val api = retrofit.create(OpenAiApi::class.java)
            OpenAiService(api)
        } else {
            OpenAiService(apiKey, Duration.ZERO)
        }
    }

    private fun prepareRequest(messages: List<LlmMsg.ChatMessage>): ChatCompletionRequest? {
        return ChatCompletionRequest.builder()
            .model("gpt-3.5-turbo")
            .temperature(temperature)
            .messages(messages.map { it.toInternal() })
            .stream(false)
            .build()
    }

    fun LlmMsg.ChatMessage.toInternal() =
        com.theokanning.openai.completion.chat.ChatMessage(this.role.name.lowercase(), this.content)

    override fun completion(messages: List<LlmMsg.ChatMessage>): String {
        val request = prepareRequest(messages)

        var result = ""
        openai.streamChatCompletion(request)
            .blockingForEach { response ->
                val completion = response.choices?.get(0)?.message
                if (completion != null && completion.content != null) {
                    result += completion.content
                }
            }

        return result
    }

    override fun streamCompletion(messages: List<LlmMsg.ChatMessage>): Flow<String> {
        val request = prepareRequest(messages)

        return callbackFlow {
            withContext(Dispatchers.IO) {
                openai.streamChatCompletion(request)
                    .doOnError { error ->
                        trySend(error.message ?: "Error occurs")
                    }
                    .blockingForEach { response ->
                        if (response.choices.isNotEmpty()) {
                            val completion = response.choices[0].message
                            if (completion != null && completion.content != null) {
                                trySend(completion.content)
                            }
                        }
                    }

                close()
            }
        }
    }

}