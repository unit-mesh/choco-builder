package cc.unitmesh.cf.infrastructure.llms.completion

import cc.unitmesh.cf.infrastructure.llms.configuration.OpenAiConfiguration
import cc.unitmesh.cf.infrastructure.llms.model.LlmMsg
import org.junit.jupiter.api.Disabled

class OpenAiProviderTest {
    @org.junit.jupiter.api.Test
    @Disabled
    fun createCompletions() {
        val config = OpenAiConfiguration()
        config.apiKey = ""
        config.apiHost = ""
        val provider = OpenAiProvider(config)
        val chatChoices = provider.simpleCompletion(listOf(LlmMsg.ChatMessage(LlmMsg.ChatRole.User, "你好")))
        println(chatChoices)
    }
}