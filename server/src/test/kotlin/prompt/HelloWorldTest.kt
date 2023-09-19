package prompt

import org.junit.jupiter.api.Test

class HelloWorldTest : LocalTestbed() {
    @Test
    fun `should say hello`() {
        println("Hello, world!")
    }
}