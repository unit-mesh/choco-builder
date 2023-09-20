package cc.unitmesh.apply

import kotlin.test.Test

class WorkflowTest {
    @Test
    fun hello_apply() {
        apply("code") {
            val data: Any = http.get("https://www.baidu.com") {
                println("hello")
            }

            // to json

            val chunks = document("filename").split()
            embedding("").search()

            llm("openai").request { println("hello") }
        }
    }
}