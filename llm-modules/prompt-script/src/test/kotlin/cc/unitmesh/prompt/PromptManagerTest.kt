package cc.unitmesh.prompt;

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.junit.jupiter.api.Test
import java.io.StringWriter

data class ChatItem(val question: String, val answer: String)

class PromptManagerTests {
    val manager = PromptManager();

    @Test
    fun should_return_prompt_when_getPrompt_called() {
        // load simple.vm from resources
        val template = this::class.java.getResource("/ui-clarify.open_ai.vm")!!.readText()
        //
        val path = this::class.java.getResource("/testdata/sample.json")!!.path
        val obj = manager.loadFile(path)!!

        // use velocity to render the template
        val context = VelocityContext()
        obj.asMap().forEach { (key, u) ->
            context.put(key, u)
        }

        val sw = StringWriter()
        Velocity.evaluate(context, sw, "#" + this.javaClass.name, template)
        val output = sw.toString()

        println(output)
    }
}