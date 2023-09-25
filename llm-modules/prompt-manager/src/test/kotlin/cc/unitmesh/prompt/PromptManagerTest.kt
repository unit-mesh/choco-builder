package cc.unitmesh.prompt;

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.junit.jupiter.api.Test
import java.io.StringWriter

class PromptManagerTests {

    @Test
    fun should_return_prompt_when_getPrompt_called() {
        // load simple.vm from resources
        val template = PromptManager::class.java.getResource("/simple.vm")!!.readText()
        // use velocity to render the template
        val context = VelocityContext()

        context.put("question", "this i a user question")

        val sw = StringWriter()
        Velocity.evaluate(context, sw, "#" + this.javaClass.name, template)
        val output = sw.toString()

        println(output)
    }
}