package cc.unitmesh.prompt;

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.junit.jupiter.api.Test
import java.io.StringWriter

data class ChatItem(val question: String, val answer: String)

class PromptManagerTests {

    @Test
    fun should_return_prompt_when_getPrompt_called() {
        // load simple.vm from resources
        val template = PromptManager::class.java.getResource("/simple.vm")!!.readText()
        // use velocity to render the template
        val context = VelocityContext()
        // Given
        val chatHistory = listOf(
            ChatItem("What is your name?", "My name is Assistant."),
            ChatItem("How old are you?", "I am 25 years old.")
        )
        val question = "What is the capital of France?"

        context.put("question", question)
        context.put("chat_history", chatHistory)

        val sw = StringWriter()
        Velocity.evaluate(context, sw, "#" + this.javaClass.name, template)
        val output = sw.toString()

        println(output)
    }
}