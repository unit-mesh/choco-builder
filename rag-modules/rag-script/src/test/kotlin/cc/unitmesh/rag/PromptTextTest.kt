package cc.unitmesh.rag;

import cc.unitmesh.docs.SampleCode
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PromptTextTest {
    @Test
    @SampleCode(name = "PromptScript", content = "")
    fun template_example() {
        val promptText =
            // start-sample
            prompt {
                paragraph("Hello World")
                codeblock("kotlin") {
                    "println(\"Hello World\")"
                }
                list(ListType.Unordered) {
                    listOf("Hello", "World")
                }
            }
        // end-sample

        promptText.toString() shouldBe """Hello World
            |```kotlin
            |println("Hello World")
            |```
            |* Hello
            |* World
            |""".trimMargin()
    }
}
