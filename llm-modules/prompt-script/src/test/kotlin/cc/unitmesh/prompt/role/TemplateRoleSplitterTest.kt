package cc.unitmesh.prompt.role;

import cc.unitmesh.prompt.executor.TemplateRoleSplitter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class TemplateRoleSplitterTest {

    @Test
    fun should_splitInputIntoSections() {
        // given
        val input = "###system###\nYou are a helpful assistant.\n\n###user###\n${'$'}{question}\n"
        val expectedSections = mapOf(
            "system" to "You are a helpful assistant.\n\n",
            "user" to "${'$'}{question}\n\n"
        )

        // when
        val sections = TemplateRoleSplitter().split(input)

        // then
        assertEquals(expectedSections, sections)
    }

    @Test
    fun should_handleRemainingContentAfterLastSection() {
        // given
        val input = "###system###\nYou are a helpful assistant.\n\n###user###\n${'$'}{question}\nRemaining content"
        val expectedSections = mapOf(
            "system" to "You are a helpful assistant.\n\n",
            "user" to "${'$'}{question}\nRemaining content\n"
        )

        // when
        val sections = TemplateRoleSplitter().split(input)

        // then
        assertEquals(expectedSections, sections)
    }

    @Test
    fun should_handleEmptyInput() {
        // given
        val input = ""
        val expectedSections = emptyMap<String, String>()

        // when
        val sections = TemplateRoleSplitter().split(input)

        // then
        assertEquals(expectedSections, sections)
    }
}