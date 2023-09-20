package prompt

import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.infrastructure.llms.completion.MockLlmProvider
import cc.unitmesh.cf.infrastructure.llms.completion.OpenAiCompletionService
import cc.unitmesh.cf.infrastructure.llms.configuration.OpenAiConfiguration
import io.github.cdimascio.dotenv.dotenv


open class LocalTestbed {
    private var completion: LlmProvider

    init {
        try {
            val dotenv = dotenv()

            OpenAiConfiguration().apply {
                apiKey = dotenv["OPENAI_API_KEY"] ?: ""
                apiHost = dotenv["OPENAI_API_HOST"]
            }.let {
                completion = OpenAiCompletionService(it)
            }
        } catch (e: Exception) {
            println("Failed to initialize OpenAI Completion")
            completion = MockLlmProvider()
        }
    }
}