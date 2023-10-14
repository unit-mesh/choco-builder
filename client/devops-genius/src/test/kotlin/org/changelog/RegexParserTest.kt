package org.changelog;

import io.kotest.matchers.shouldBe
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

    @Test
    fun `references - should match a simple reference`() {
        val references = RegexParser.getParserRegexes(ParserOptions(
            referenceActions = listOf("Closes")
        )).references
        val match = references.find("closes #1")

        assertEquals("closes #1", match?.value)
        assertEquals("closes", match?.groups?.get(1)?.value)
        assertEquals("#1", match?.groups?.get(2)?.value)
    }

    @Test
    fun `references - should be case insensitive`() {
        val references = RegexParser.getParserRegexes(ParserOptions(
            referenceActions = listOf("Closes")
        )).references
        val match = references.find("ClOsEs #1")

        assertEquals("ClOsEs #1", match?.value)
        assertEquals("ClOsEs", match?.groups?.get(1)?.value)
        assertEquals("#1", match?.groups?.get(2)?.value)
    }

    @Test
    fun `references - should not match if keywords does not present`() {
        val references = RegexParser.getParserRegexes(ParserOptions(
            referenceActions = listOf("Close")
        )).references
        val match = references.find("Closes #1")

        assertNull(match)
    }

    @Test
    fun `references - should take multiple reference keywords`() {
        val references = RegexParser.getParserRegexes(ParserOptions(
            referenceActions = listOf(" Closes", "amends", "fixes")
        )).references
        val match = references.find("amends #1")

        assertEquals("amends #1", match?.value)
        assertEquals("amends", match?.groups?.get(1)?.value)
        assertEquals("#1", match?.groups?.get(2)?.value)
    }

    @Test
    fun `references - should match multiple references`() {
        val references = RegexParser.getParserRegexes(ParserOptions(
            referenceActions = listOf("Closes", "amends")
        )).references
        val message = "Closes #1 amends #2; closes bug #4"
        val match = references.find(message)

        assertEquals("Closes #1 ", match?.value)
        assertEquals("Closes", match?.groups?.get(1)?.value)
        assertEquals("#1 ", match?.groups?.get(2)?.value)
    }

    @Test
    fun `references - should match references with mixed content, like JIRA tickets`() {
        val references = RegexParser.getParserRegexes(ParserOptions(
            referenceActions = listOf("Closes", "amends")
        )).references
        val message = "Closes #JIRA-123 amends #MY-OTHER-PROJECT-123; closes bug #4"
        val match = references.find(message)

        assertEquals("Closes #JIRA-123 ", match?.value)
        assertEquals("Closes", match?.groups?.get(1)?.value)
        assertEquals("#JIRA-123 ", match?.groups?.get(2)?.value)
    }

    @Test
    fun `references - should reference an issue without an action`() {
        val references = RegexParser.getParserRegexes(ParserOptions()).references
        val body = "gh-1, prefix-3, Closes gh-6"
        val match = references.find(body)

        assertEquals(body, match?.value)
        assertEquals("", match?.groups?.get(1)?.value)
        assertEquals(body, match?.groups?.get(2)?.value)
    }

    @Test
    fun `references - should ignore whitespace`() {
        val references = RegexParser.getParserRegexes(ParserOptions(
            referenceActions = listOf(" Closes", "amends ", "", " fixes ", "   ")
        )).references

        val match = references.findAll("closes #1, amends #2, fixes #3").map {
            it.value
        }.toList()

        match shouldBe listOf(
            "closes #1, ",
            "amends #2, ",
            "fixes #3"
        )
    }

    @Test
    fun `referenceParts - should match simple reference parts`() {
        val referenceParts = RegexParser.getParserRegexes(
            ParserOptions(
                issuePrefixes = listOf("#")
            )
        ).referenceParts

        val match = referenceParts.find("#1")

        assertEquals("#1", match?.value)
        assertEquals(null, match?.groups?.get(1)?.value)
        assertEquals("#", match?.groups?.get(2)?.value)
        assertEquals("1", match?.groups?.get(3)?.value)
    }

    @Test
    fun `referenceParts - should reference an issue in parenthesis`() {
        val referenceParts = RegexParser.getParserRegexes(
            ParserOptions(
                issuePrefixes = listOf("#")
            )
        ).referenceParts
        val body = "#27), pinned shelljs to version that works with nyc (#30)"
        val match = referenceParts.find(body)

        assertEquals("#27", match?.value)
        assertEquals(null, match?.groups?.get(1)?.value)
        assertEquals("#", match?.groups?.get(2)?.value)
        assertEquals("27", match?.groups?.get(3)?.value)
    }

    @Test
    fun `referenceParts - should match reference parts with something else`() {
        val referenceParts = RegexParser.getParserRegexes(
            ParserOptions(
                issuePrefixes = listOf("#")
            )
        ).referenceParts
        val match = referenceParts.find("something else #1")

        assertEquals("something else #1", match?.value)
        assertEquals(null, match?.groups?.get(1)?.value)
        assertEquals("#", match?.groups?.get(2)?.value)
        assertEquals("1", match?.groups?.get(3)?.value)
    }

    @Test
    fun `referenceParts - should match reference parts with a repository`() {
        val referenceParts = RegexParser.getParserRegexes(
            ParserOptions(
                issuePrefixes = listOf("#")
            )
        ).referenceParts
        val match = referenceParts.find("repo#1")

        assertEquals("repo#1", match?.value)
        assertEquals("repo", match?.groups?.get(1)?.value)
        assertEquals("#", match?.groups?.get(2)?.value)
        assertEquals("1", match?.groups?.get(3)?.value)
    }

    @Test
    fun `referenceParts - should match JIRA-123 like reference parts`() {
        val referenceParts = RegexParser.getParserRegexes(
            ParserOptions(
                issuePrefixes = listOf("#")
            )
        ).referenceParts
        val match = referenceParts.find("#JIRA-123")

        assertEquals("#JIRA-123", match?.value)
        assertEquals(null, match?.groups?.get(1)?.value)
        assertEquals("#", match?.groups?.get(2)?.value)
        assertEquals("JIRA-123", match?.groups?.get(3)?.value)
    }

    @Test
    fun `referenceParts - should not match MY-€#%#&-123 mixed symbol reference parts`() {
        val referenceParts = RegexParser.getParserRegexes(
            ParserOptions(
                issuePrefixes = listOf("#")
            )
        ).referenceParts
        val match = referenceParts.find("#MY-€#%#&-123")

        assertNull(match)
    }

    @Test
    fun `referenceParts - should match issues with customized prefix`() {
        val referenceParts = RegexParser.getParserRegexes(
            ParserOptions(
                issuePrefixes = listOf("gh-", "prefix-")
            )
        ).referenceParts
        val body = "closes gh-1, amends #2, fixes prefix-3"
        val match = referenceParts.find(body)

        assertEquals("closes gh-1", match?.value)
        assertEquals(null, match?.groups?.get(1)?.value)
        assertEquals("gh-", match?.groups?.get(2)?.value)
        assertEquals("1", match?.groups?.get(3)?.value)
    }

    @Test
    fun `referenceParts - should match nothing if there is no customized prefix`() {
        val referenceParts = RegexParser.getParserRegexes(
            ParserOptions(
                issuePrefixes = listOf("gh-", "prefix-")
            )
        ).referenceParts
        val body = "closes gh-1, amends #2, fixes prefix-3"
        val match = referenceParts.find(body)

        assertEquals("closes gh-1", match?.value)
        assertEquals(null, match?.groups?.get(1)?.value)
        assertEquals("gh-", match?.groups?.get(2)?.value)
        assertEquals("1", match?.groups?.get(3)?.value)
    }

    @Test
    fun `referenceParts - should match issues with customized prefix 2`() {
        val referenceParts = RegexParser.getParserRegexes(
            ParserOptions(
                issuePrefixes = listOf("gh-", "prefix-")
            )
        ).referenceParts
        val body = "closes gh-1, amends #2, fixes prefix-3"
        val match = referenceParts.find(body)

        assertEquals("closes gh-1", match?.value)
        assertEquals(null, match?.groups?.get(1)?.value)
        assertEquals("gh-", match?.groups?.get(2)?.value)
        assertEquals("1", match?.groups?.get(3)?.value)
    }

    @Test
    fun `mentions - should match a simple mention`() {
        val mentions = RegexParser.getParserRegexes(ParserOptions()).mentions
        val match = mentions.find("@kentcdodds")

        assertEquals("@kentcdodds", match?.value)
        assertEquals("kentcdodds", match?.groups?.get(1)?.value)
    }

    // mentions
    //   it('should match basically mention', () => {
    //        const body = 'Thanks!! @someone'
    //        const { mentions } = getParserRegexes()
    //        const match = mentions.exec(body)
    //
    //        expect(match?.[0]).toBe('@someone')
    //        expect(match?.[1]).toBe('someone')
    //      })
    @Test
    fun `mentions - should match basically mention`() {
        val mentions = RegexParser.getParserRegexes(ParserOptions()).mentions
        val match = mentions.find("Thanks!! @someone")

        assertEquals("@someone", match?.value)
        assertEquals("someone", match?.groups?.get(1)?.value)
    }

    @Test
    fun `mentions - should match mention with hyphen`() {
        val mentions = RegexParser.getParserRegexes(ParserOptions()).mentions
        val match = mentions.find("Thanks!! @some-one")

        assertEquals("@some-one", match?.value)
        assertEquals("some-one", match?.groups?.get(1)?.value)
    }

    @Test
    fun `mentions - should match mention with underscore`() {
        val mentions = RegexParser.getParserRegexes(ParserOptions()).mentions
        val match = mentions.find("Thanks!! @some_one")

        assertEquals("@some_one", match?.value)
        assertEquals("some_one", match?.groups?.get(1)?.value)
    }

    @Test
    fun `mentions - should match mention with parentheses`() {
        val mentions = RegexParser.getParserRegexes(ParserOptions()).mentions
        val match = mentions.find("Fix feature1 (by @someone)")

        assertEquals("@someone", match?.value)
        assertEquals("someone", match?.groups?.get(1)?.value)
    }

    @Test
    fun `mentions - should match mention with brackets`() {
        val mentions = RegexParser.getParserRegexes(ParserOptions()).mentions
        val match = mentions.find("Fix feature1 [by @someone]")

        assertEquals("@someone", match?.value)
        assertEquals("someone", match?.groups?.get(1)?.value)
    }

    @Test
    fun `mentions - should match mention with braces`() {
        val mentions = RegexParser.getParserRegexes(ParserOptions()).mentions
        val match = mentions.find("Fix feature1 {by @someone}")

        assertEquals("@someone", match?.value)
        assertEquals("someone", match?.groups?.get(1)?.value)
    }

    @Test
    fun `mentions - should match mention with angle brackets`() {
        val mentions = RegexParser.getParserRegexes(ParserOptions()).mentions
        val match = mentions.find("Fix feature1 by <@someone>")

        assertEquals("@someone", match?.value)
        assertEquals("someone", match?.groups?.get(1)?.value)
    }
}