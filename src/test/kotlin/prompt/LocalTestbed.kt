package prompt

import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider
import cc.unitmesh.cf.infrastructure.llms.completion.MockProvider
import cc.unitmesh.cf.infrastructure.llms.completion.OpenAiProvider
import cc.unitmesh.cf.infrastructure.llms.configuration.OpenAiConfiguration
import io.github.cdimascio.dotenv.dotenv


open class LocalTestbed {
    private var completion: LlmProvider

    init {
        try {
            val dotenv = dotenv()

            OpenAiConfiguration().apply {
                apiKey = dotenv["OPENAI_KEY"] ?: ""
                apiHost = dotenv["OPENAI_SERVER_ADDRESS"]
            }.let {
                completion = OpenAiProvider(it)
            }
        } catch (e: Exception) {
            println("Failed to initialize OpenAI Completion")
            completion = MockProvider()
        }
    }
}