package cc.unitmesh.openai

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.cf.core.llms.LlmProvider
import com.theokanning.openai.client.OpenAiApi
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.service.OpenAiService
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
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
                val completion = response.choices[0].message
                if (completion != null && completion.content != null) {
                    result += completion.content
                }
            }

        return result
    }

    override fun streamCompletion(messages: List<LlmMsg.ChatMessage>): Flowable<String> {
        val request = prepareRequest(messages)

        return Flowable.create({ emitter ->
            val disposable = openai.streamChatCompletion(request)
                .doOnSubscribe {}
                .subscribe(
                    { response ->
                        val completion = response.choices[0].message
                        if (completion != null && completion.content != null) {
                            emitter.onNext(completion.content)
                        }
                    },
                    { error ->
                        emitter.onError(error)
                    },
                    {
                        emitter.onComplete()
                    }
                )

            emitter.setCancellable {
                // This will be called when the Flowable is unsubscribed
                disposable.dispose()
            }
        }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

}