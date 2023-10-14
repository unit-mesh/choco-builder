package org.cc

import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Ignore
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


    @Test
    fun `supports multiline BREAKING CHANGES, via continuation`() {
        val json = this::class.java.classLoader.getResource("commits/supports-multiline-breaking-changes-via-continuation.json").readText()
        val data = Json.decodeFromString(Node.serializer(), json)
        val parsed = Parser("fix: address major bug\nBREAKING CHANGE: first line of breaking change\n second line of breaking change\n third line of breaking change").parse()
        parsed shouldBe data
    }
}