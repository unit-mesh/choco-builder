package cc.unitmesh.cf.infrastructure.llms.completion

import cc.unitmesh.cf.infrastructure.llms.configuration.OpenAiConfiguration
import cc.unitmesh.cf.core.llms.LlmMsg
import org.junit.jupiter.api.Disabled

class OpenAiCompletionServiceTest {
    @org.junit.jupiter.api.Test
    @Disabled
    fun createCompletions() {
        val config = OpenAiConfiguration()
        config.apiKey = ""
        config.apiHost = ""
        val provider = OpenAiCompletionService(config)
        val chatChoices = provider.completion(listOf(LlmMsg.ChatMessage(LlmMsg.ChatRole.User, "你好")))
        println(chatChoices)
    }
}