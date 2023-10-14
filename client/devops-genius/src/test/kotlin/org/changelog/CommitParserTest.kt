package org.changelog;

import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class CommitParserTest  {
    val customOptions: ParserOptions = ParserOptions(
        revertPattern = Regex("^Revert\\s\"([\\s\\S]*)\"\\s*This reverts commit (.*)\\.$"),
        revertCorrespondence = listOf("header", "hash"),
        fieldPattern = Regex("^-(.*?)-$"),
        headerPattern = Regex("^(\\w*)(?:\\(([\\w$.*\\- ]*)\\))?: (.*)$"),
        headerCorrespondence = listOf("type", "scope", "subject"),
        noteKeywords = listOf("BREAKING AMEND"),
        issuePrefixes = listOf("#", "gh-"),
        referenceActions = listOf("kill", "kills", "killed", "handle", "handles", "handled")
    )

    val customParser = CommitParser(customOptions)

    @Test
    fun shouldWork() {
        val parser = CommitParser()
        val commit = "feat(ng-list): Allow custom separator\n" +
                "bla bla bla\n\n" +
                "Closes #123\nCloses #25\nFixes #33\n"
        val result = parser.parse(commit)

        println(result)

        result.header shouldBe "feat(ng-list): Allow custom separator"
        result.footer shouldBe "Closes #123\nCloses #25\nFixes #33"
        result.references shouldBe listOf(
            CommitReference(
                action = "Closes",
                issue = "123",
                owner = null,
                prefix = "#",
                raw = "#123",
                repository = null
            ),
            CommitReference(
                action = "Closes",
                issue = "25",
                owner = null,
                prefix = "#",
                raw = "#25",
                repository = null
            ),
            CommitReference(
                action = "Fixes",
                issue = "33",
                owner = null,
                prefix = "#",
                raw = "#33",
                repository = null
            )
        )
    }
}
