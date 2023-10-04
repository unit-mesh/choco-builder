package cc.unitmesh.docs;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RunnerTest {

    @Test
    fun `should convert uppercase to dash`() {
        // given
        val name = "HelloWorld"

        // when
        val result = Runner.uppercaseToDash(name)

        // then
        assertEquals("hello-world", result)
    }

    @Test
    fun `should handle empty string`() {
        // given
        val name = ""

        // when
        val result = Runner.uppercaseToDash(name)

        // then
        assertEquals("", result)
    }

    @Test
    fun `should handle single uppercase character`() {
        // given
        val name = "A"

        // when
        val result = Runner.uppercaseToDash(name)

        // then
        assertEquals("a", result)
    }

    @Test
    fun `should handle lowercase string`() {
        // given
        val name = "helloworld"

        // when
        val result = Runner.uppercaseToDash(name)

        // then
        assertEquals("helloworld", result)
    }
}
