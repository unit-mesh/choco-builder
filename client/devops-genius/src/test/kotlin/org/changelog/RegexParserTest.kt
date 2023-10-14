package org.changelog;

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RegexParserTest {

    @Test
    fun `should match a simple note`() {
        val notes = RegexParser.getParserRegexes(ParserOptions(
            noteKeywords = listOf("Breaking News", "Breaking Change")
        )).notes
        val match = notes.find("Breaking News: This is so important.")

        assertEquals("Breaking News: This is so important.", match?.value)
        assertEquals("Breaking News", match?.groups?.get(1)?.value)
        assertEquals("This is so important.", match?.groups?.get(2)?.value)
    }

    @Test
    fun `should match notes with customized pattern`() {
        val notes = RegexParser.getParserRegexes(ParserOptions(
            noteKeywords = listOf("BREAKING CHANGE", "BREAKING-CHANGE"),
            notesPattern = { noteKeywords -> Regex("^[\\s|*]*($noteKeywords)[:\\s]+(?:\\[.*\\] )(.*)", RegexOption.IGNORE_CASE) }
        )).notes
        val message = "BREAKING CHANGE: [Do not match this prefix.] This is so important."
        val match = notes.find(message)

        assertEquals(message, match?.value)
        assertEquals("BREAKING CHANGE", match?.groups?.get(1)?.value)
        assertEquals("This is so important.", match?.groups?.get(2)?.value)
    }

    @Test
    fun `should be case insensitive`() {
        val notes = RegexParser.getParserRegexes(ParserOptions(
            noteKeywords = listOf("Breaking News", "Breaking Change")
        )).notes
        val match = notes.find("BREAKING NEWS: This is so important.")

        assertEquals("BREAKING NEWS: This is so important.", match?.value)
        assertEquals("BREAKING NEWS", match?.groups?.get(1)?.value)
        assertEquals("This is so important.", match?.groups?.get(2)?.value)
    }

    @Test
    fun `should ignore whitespace`() {
        val notes = RegexParser.getParserRegexes(ParserOptions(
            noteKeywords = listOf(" Breaking News", "Breaking Change ", "", " Breaking SOLUTION ", "  "),
            issuePrefixes = listOf("#")
        )).notes
        val match = notes.find("Breaking News: This is so important.")

        assertEquals("Breaking News: This is so important.", match?.value)
        assertEquals("Breaking News", match?.groups?.get(1)?.value)
        assertEquals("This is so important.", match?.groups?.get(2)?.value)
    }

    @Test
    fun `should not accidentally match in a sentence`() {
        val notes = RegexParser.getParserRegexes(ParserOptions(
            noteKeywords = listOf(" Breaking News"),
            issuePrefixes = listOf("#")
        )).notes
        val match = notes.find("This is a breaking news: So important.")

        assertNull(match)
    }

    @Test
    fun `should not match if there is text after noteKeywords`() {
        val notes = RegexParser.getParserRegexes(ParserOptions(
            noteKeywords = listOf("BREAKING CHANGE", "BREAKING-CHANGE"),
            issuePrefixes = listOf("#")
        )).notes
        val match = notes.find("BREAKING CHANGES: Wow.")

        assertNull(match)
    }

}