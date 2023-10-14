package org.cc

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.test.Test

class ParserTest {
    @Test
    fun `parses summary with no scope`() {
        val json = this::class.java.classLoader.getResource("commits/parses-summary-with-no-scope.json").readText()

        val data = Json.decodeFromString(Node.serializer(), json)
        val node = Parser("fix: a really weird bug").parse()
        node shouldBe data
    }

    @Test
    fun `parses summary with scope`() {
        val json = this::class.java.classLoader.getResource("commits/parses-summary-with-scope.json").readText()
        val data = Json.decodeFromString(Node.serializer(), json)
        val parsed = Parser("feat(parser): add support for scopes").parse()
        parsed shouldBe data
    }

    // supports multiline BREAKING CHANGES, via continuation
    @Test
    fun `supports multiline BREAKING CHANGES, via continuation`() {
        val json = this::class.java.classLoader.getResource("commits/supports-multiline-breaking-changes-via-continuation.json").readText()
        val data = Json.decodeFromString(Node.serializer(), json)
        val parsed = Parser("fix: address major bug\nBREAKING CHANGE: first line of breaking change\n second line of breaking change\n third line of breaking change").parse()
        parsed shouldBe data
    }

    // parses summary with multiple spaces after separator
    @Test
    fun `parses summary with multiple spaces after separator`() {
        val json = this::class.java.classLoader.getResource("commits/parses-summary-with-multiple-spaces-after-separator.json").readText()
        val data = Json.decodeFromString(Node.serializer(), json)
        val parsed = Parser("feat(tree):    add whitespace node").parse()
        parsed shouldBe data
    }

    // parses commit summary footer
    @Test
    fun `parses commit summary footer`() {
        val json = this::class.java.classLoader.getResource("commits/parses-commit-summary-footer.json").readText()
        val data = Json.decodeFromString(Node.serializer(), json)
        val parsed = Parser("chore: contains multiple commits\nfix(parser): address bug with parser").parse()
        parsed shouldBe data
    }

    // allows for multiple newlines between summary and body
    @Test
    fun `allows for multiple newlines between summary and body`() {
        val json = this::class.java.classLoader.getResource("commits/allows-for-multiple-newlines-between-summary-and-body.json").readText()
        val data = Json.decodeFromString(Node.serializer(), json)
        val parsed = Parser("fix: address major bug\n\nthis is a free form body of text").parse()
        parsed shouldBe data
    }
}
