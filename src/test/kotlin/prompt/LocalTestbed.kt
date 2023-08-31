package prompt

import cc.unitmesh.cf.infrastructure.llms.completion.CompletionProvider
import cc.unitmesh.cf.infrastructure.llms.completion.OpenAiCompletion
import cc.unitmesh.cf.infrastructure.llms.configuration.OpenAiConfiguration
import io.github.cdimascio.dotenv.dotenv


open class LocalTestbed {
    private var completion: CompletionProvider

    init {
        val dotenv = dotenv {
            directory = "."
            filename = ".env"
        }

        OpenAiConfiguration().apply {
            apiKey = dotenv["OPENAI_KEY"] ?: ""
            serverAddress = dotenv["OPENAI_SERVER_ADDRESS"]
        }.let {
            completion = OpenAiCompletion(it)
        }
    }
}