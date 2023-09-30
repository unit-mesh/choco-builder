package cc.unitmesh.prompt;

import com.github.ajalt.clikt.testing.test
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.Ignore

class PromptScriptCommandTest {
    @Test
    @Ignore
    fun should_print_test() {
        // given
        val command = PromptScriptCommand()
        val inputFile = File(".").resolve("../../examples/promptscript/prompt.unit-mesh.yml").absolutePath.toString()
        val result = command.test("--input $inputFile")


        result.statusCode shouldBe 0
        // when
//        command.run()
    }
}