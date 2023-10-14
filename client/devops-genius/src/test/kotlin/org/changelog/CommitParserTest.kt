package org.changelog;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CommitParserTest {
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

    @Test
    fun `should parse references from header`() {
        val commit = "Subject #1"
        val result: Commit = CommitParser().parse(commit)

        result.references shouldBe listOf(
            CommitReference(
                action = null,
                issue = "1",
                owner = null,
                prefix = "#",
                raw = "Subject #1",
                repository = null
            )
        )
    }

    @Test
    fun `should parse slash in the header with default headerPattern option`() {
        val commit = "feat(hello/world): message"
        val result: Commit = CommitParser().parse(commit)

        result.meta["type"] shouldBe "feat"
        result.meta["scope"] shouldBe "hello/world"
        result.meta["subject"] shouldBe "message"
    }

//    @Test
//    fun `should not be subject to ReDos`() {
//        val input = "b${"\r\n".repeat(1000000)}b"
//        val result: Commit = customParser.parse(input)
//    }

    @Test
    fun `should trim extra newlines`() {
        val commit = customParser.parse(
            "\n\n\n\n\n\n\nfeat(scope): broadcast \$destroy event on scope destruction\n\n\n"
                    + "\n\n\nperf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "\n\n\nwhen destroying 10k nested scopes where each scope has a \$destroy listener\n\n"
                    + "\n\n\n\nBREAKING AMEND: some breaking change\n"
                    + "\n\n\n\nBREAKING AMEND: An awesome breaking change\n\n\n```\ncode here\n```"
                    + "\n\nKills #1\n"
                    + "\n\n\nkilled #25\n\n\n\n\n"
        )

        commit.header shouldBe "feat(scope): broadcast \$destroy event on scope destruction"
        commit.meta["type"] shouldBe "feat"
        commit.meta["scope"] shouldBe "scope"
        commit.meta["subject"] shouldBe "broadcast \$destroy event on scope destruction"
        commit.body shouldBe "perf testing shows that in chrome this change adds 5-15% overhead\n\n\n\nwhen destroying 10k nested scopes where each scope has a \$destroy listener"
        commit.footer shouldBe "BREAKING AMEND: some breaking change\n\n\n\n\nBREAKING AMEND: An awesome breaking change\n\n\n```\ncode here\n```\n\nKills #1\n\n\n\nkilled #25"
        commit.notes shouldBe listOf(
            CommitNote(
                title = "BREAKING AMEND",
                text = "some breaking change"
            ),
            CommitNote(
                title = "BREAKING AMEND",
                text = "An awesome breaking change\n\n\n```\ncode here\n```"
            )
        )
        commit.references shouldBe listOf(
            CommitReference(
                action = "Kills",
                issue = "1",
                owner = null,
                prefix = "#",
                raw = "#1",
                repository = null
            ),
            CommitReference(
                action = "killed",
                issue = "25",
                owner = null,
                prefix = "#",
                raw = "#25",
                repository = null
            )
        )
    }

    @Test
    fun `should ignore gpg signature lines`() {
        val commit = customParser.parse(
            "gpg: Signature made Thu Oct 22 12:19:30 2020 EDT\n"
                    + "gpg:                using RSA key ABCDEF1234567890\n"
                    + "gpg: Good signature from \"Author <author@example.com>\" [ultimate]\n"
                    + "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\n"
                    + "BREAKING AMEND: some breaking change\n"
                    + "Kills #1\n"
        )

        commit.header shouldBe "feat(scope): broadcast \$destroy event on scope destruction"
        commit.meta["type"] shouldBe "feat"
        commit.meta["scope"] shouldBe "scope"
        commit.meta["subject"] shouldBe "broadcast \$destroy event on scope destruction"
        commit.body shouldBe "perf testing shows that in chrome this change adds 5-15% overhead\nwhen destroying 10k nested scopes where each scope has a \$destroy listener"
        commit.footer shouldBe "BREAKING AMEND: some breaking change\nKills #1"
        commit.notes shouldBe listOf(
            CommitNote(
                title = "BREAKING AMEND",
                text = "some breaking change"
            )
        )
        commit.references shouldBe listOf(
            CommitReference(
                action = "Kills",
                issue = "1",
                owner = null,
                prefix = "#",
                raw = "#1",
                repository = null
            )
        )
    }

    @Test
    fun `should parse header with scope`() {
        val commit = customParser.parse("feat(scope): broadcast \$destroy event on scope destruction")

        commit.header shouldBe "feat(scope): broadcast \$destroy event on scope destruction"
        commit.meta["type"] shouldBe "feat"
        commit.meta["scope"] shouldBe "scope"
        commit.meta["subject"] shouldBe "broadcast \$destroy event on scope destruction"
    }

    @Test
    fun `should parse header without scope`() {
        val commit = customParser.parse("feat: broadcast \$destroy event on scope destruction")

        commit.header shouldBe "feat: broadcast \$destroy event on scope destruction"
        commit.meta["type"] shouldBe "feat"
        commit.meta["scope"] shouldBe null
        commit.meta["subject"] shouldBe "broadcast \$destroy event on scope destruction"
    }

    @Test
    fun `should parse body`() {
        val commit = customParser.parse(
            "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener"
        )

        commit.header shouldBe "feat(scope): broadcast \$destroy event on scope destruction"
        commit.meta["type"] shouldBe "feat"
        commit.meta["scope"] shouldBe "scope"
        commit.meta["subject"] shouldBe "broadcast \$destroy event on scope destruction"
        commit.body shouldBe "perf testing shows that in chrome this change adds 5-15% overhead\nwhen destroying 10k nested scopes where each scope has a \$destroy listener"
    }
}
