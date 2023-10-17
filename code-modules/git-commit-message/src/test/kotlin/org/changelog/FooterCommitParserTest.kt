package org.changelog;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test


class FotterCommitParserTest {
    val customParser = CommitParser(customOptions)

    @Test
    fun `should be null if not found`() {
        val commit = customParser.parse("header")

        commit.footer shouldBe null
    }

    @Test
    fun `should parse footer`() {
        val commit = customParser.parse(
            "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\n"
                    + "BREAKING AMEND: some breaking change\n"
                    + "Kills #1, #123\n"
                    + "killed #25\n"
                    + "handle #33, Closes #100, Handled #3 kills repo#77\n"
                    + "kills stevemao/conventional-commits-parser#1"
        )

        commit.footer shouldBe (
                "BREAKING AMEND: some breaking change\n"
                        + "Kills #1, #123\n"
                        + "killed #25\n"
                        + "handle #33, Closes #100, Handled #3 kills repo#77\n"
                        + "kills stevemao/conventional-commits-parser#1")
    }

    @Test
    fun `should return empty notes`() {
        val commit = customParser.parse(
            "chore: some chore\n"
        )

        commit.notes shouldBe emptyList()
    }

    @Test
    fun `should parse important notes`() {
        val commit = customParser.parse(
            "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\n"
                    + "BREAKING AMEND: some breaking change\n"
                    + "Kills #1, #123\n"
                    + "killed #25\n"
                    + "handle #33, Closes #100, Handled #3 kills repo#77\n"
                    + "kills stevemao/conventional-commits-parser#1"
        )

        commit.notes[0] shouldBe
                CommitNote(
                    title = "BREAKING AMEND",
                    text = "some breaking change"
                )
    }

    @Test
    fun `should parse important notes with more than one paragraphs`() {
        val commit = customParser.parse(
            "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\n"
                    + "BREAKING AMEND:\n"
                    + "some breaking change\n"
                    + "some other breaking change\n"
                    + "Kills #1, #123\n"
                    + "killed #25\n"
                    + "handle #33, Closes #100, Handled #3"
        )

        commit.notes[0] shouldBe
                CommitNote(
                    title = "BREAKING AMEND",
                    text = "some breaking change\nsome other breaking change"
                )
    }

    @Test
    fun `should parse important notes that start with asterisks (for squash commits)`() {
        val expectedText = ("Previously multiple template bindings on one element\n"
                + "(ex. `<div *ngIf='..' *ngFor='...'>`) were allowed but most of the time\n"
                + "were leading to undesired result. It is possible that a small number\n"
                + "of applications will see template parse errors that shuld be fixed by\n"
                + "nesting elements or using `<template>` tags explicitly.")
        val text = "$expectedText\nCloses #9462"
        val parser = CommitParser(
            customOptions.copy(
                noteKeywords = listOf("BREAKING CHANGE", "BREAKING-CHANGE")
            )
        )
        val commit = parser.parse(
            "fix(core): report duplicate template bindings in templates\n"
                    + "\n"
                    + "Fixes #7315\n"
                    + "\n"
                    + "* BREAKING CHANGE:\n"
                    + "\n$text"
        )
        val expected = CommitNote(
            title = "BREAKING CHANGE",
            text = expectedText
        )

        commit.references.map { it.issue } shouldBe listOf("7315", "9462")
        commit.notes[0] shouldBe expected
    }

    @Test
    fun `should not treat it as important notes if there are texts after noteKeywords`() {
        val parser = CommitParser(
            ParserOptions(
                noteKeywords = listOf("BREAKING CHANGE", "BREAKING-CHANGE")
            )
        )
        val commit = parser.parse(
            "fix(core): report duplicate template bindings in templates\n"
                    + "\n"
                    + "Fixes #7315\n"
                    + "\n"
                    + "BREAKING CHANGES:\n"
                    + "\n"
                    + "Previously multiple template bindings on one element\n"
                    + "(ex. `<div *ngIf='..' *ngFor='...'>`) were allowed but most of the time\n"
                    + "were leading to undesired result. It is possible that a small number\n"
                    + "of applications will see template parse errors that shuld be fixed by\n"
                    + "nesting elements or using `<template>` tags explicitly.\n"
                    + "\n"
                    + "Closes #9462"
        )

        commit.notes shouldBe emptyList()
    }

    @Test
    fun `should parse references as empty if not found`() {
        val commit = customParser.parse(
            "chore: some chore\n"
        )

        commit.references shouldBe emptyList()
    }

    @Test
    fun `should parse references`() {
        val commit = customParser.parse(
            "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\n"
                    + "BREAKING AMEND: some breaking change\n"
                    + "Kills #1, #123\n"
                    + "killed #25\n"
                    + "handle #33, Closes #100, Handled #3 kills repo#77\n"
                    + "kills stevemao/conventional-commits-parser#1"
        )

        commit.references shouldBe listOf(
            CommitReference(
                action = "Kills",
                owner = null,
                repository = null,
                issue = "1",
                raw = "#1",
                prefix = "#"
            ),
            CommitReference(
                action = "Kills",
                owner = null,
                repository = null,
                issue = "123",
                raw = ", #123",
                prefix = "#"
            ),
            CommitReference(
                action = "killed",
                owner = null,
                repository = null,
                issue = "25",
                raw = "#25",
                prefix = "#"
            ),
            CommitReference(
                action = "handle",
                owner = null,
                repository = null,
                issue = "33",
                raw = "#33",
                prefix = "#"
            ),
            CommitReference(
                action = "handle",
                owner = null,
                repository = null,
                issue = "100",
                raw = ", Closes #100",
                prefix = "#"
            ),
            CommitReference(
                action = "Handled",
                owner = null,
                repository = null,
                issue = "3",
                raw = "#3",
                prefix = "#"
            ),
            CommitReference(
                action = "kills",
                owner = null,
                repository = "repo",
                issue = "77",
                raw = "repo#77",
                prefix = "#"
            ),
            CommitReference(
                action = "kills",
                owner = "stevemao",
                repository = "conventional-commits-parser",
                issue = "1",
                raw = "stevemao/conventional-commits-parser#1",
                prefix = "#"
            )
        )
    }

    @Test
    fun `should reference an issue without an action`() {
        val parser = CommitParser(
            ParserOptions(
                revertPattern = Regex("^Revert\\s\"([\\s\\S]*)\"\\s*This reverts commit (.*)\\.$"),
                revertCorrespondence = listOf("header", "hash"),
                fieldPattern = Regex("^-(.*?)-$"),
                headerPattern = Regex("^(\\w*)(?:\\(([\\w$.*\\- ]*)\\))?: (.*)$"),
                headerCorrespondence = listOf(
                    "type",
                    "scope",
                    "subject"
                ),
                noteKeywords = listOf("BREAKING AMEND"),
                issuePrefixes = listOf("#", "gh-")
            )
        )
        val commit = parser.parse(
            "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\n"
                    + "Kills #1, gh-123\n"
                    + "what\n"
                    + "* #25\n"
                    + "* #33, maybe gh-100, not sure about #3\n"
        )

        commit.references shouldBe listOf(
            CommitReference(
                action = null,
                owner = null,
                repository = null,
                issue = "1",
                raw = "Kills #1",
                prefix = "#"
            ),
            CommitReference(
                action = null,
                owner = null,
                repository = null,
                issue = "123",
                raw = ", gh-123",
                prefix = "gh-"
            ),
            CommitReference(
                action = null,
                owner = null,
                repository = null,
                issue = "25",
                raw = "* #25",
                prefix = "#"
            ),
            CommitReference(
                action = null,
                owner = null,
                repository = null,
                issue = "33",
                raw = "* #33",
                prefix = "#"
            ),
            CommitReference(
                action = null,
                owner = null,
                repository = null,
                issue = "100",
                raw = ", maybe gh-100",
                prefix = "gh-"
            ),
            CommitReference(
                action = null,
                owner = null,
                repository = null,
                issue = "3",
                raw = ", not sure about #3",
                prefix = "#"
            )
        )
    }

    //       it('should put everything after references in footer', () => {
    //        const commit = customParser.parse(
    //          'feat(scope): broadcast $destroy event on scope destruction\n'
    //          + 'perf testing shows that in chrome this change adds 5-15% overhead\n'
    //          + 'when destroying 10k nested scopes where each scope has a $destroy listener\n'
    //          + 'Kills #1, #123\n'
    //          + 'what\n'
    //          + 'killed #25\n'
    //          + 'handle #33, Closes #100, Handled #3\n'
    //          + 'other'
    //        )
    //
    //        expect(commit.footer).toBe('Kills #1, #123\nwhat\nkilled #25\nhandle #33, Closes #100, Handled #3\nother')
    //      })
    //
    //      it('should parse properly if important notes comes after references', () => {
    //        const commit = customParser.parse(
    //          'feat(scope): broadcast $destroy event on scope destruction\n'
    //          + 'perf testing shows that in chrome this change adds 5-15% overhead\n'
    //          + 'when destroying 10k nested scopes where each scope has a $destroy listener\n'
    //          + 'Kills #1, #123\n'
    //          + 'BREAKING AMEND: some breaking change\n'
    //        )
    //
    //        expect(commit.notes[0]).toEqual({
    //          title: 'BREAKING AMEND',
    //          text: 'some breaking change'
    //        })
    //        expect(commit.references).toEqual([
    //          {
    //            action: 'Kills',
    //            owner: null,
    //            repository: null,
    //            issue: '1',
    //            raw: '#1',
    //            prefix: '#'
    //          },
    //          {
    //            action: 'Kills',
    //            owner: null,
    //            repository: null,
    //            issue: '123',
    //            raw: ', #123',
    //            prefix: '#'
    //          }
    //        ])
    //        expect(commit.footer).toBe('Kills #1, #123\nBREAKING AMEND: some breaking change')
    //      })
    @Test
    fun `should put everything after references in footer`() {
        val commit = customParser.parse(
            "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\n"
                    + "Kills #1, #123\n"
                    + "what\n"
                    + "killed #25\n"
                    + "handle #33, Closes #100, Handled #3\n"
                    + "other"
        )

        commit.footer shouldBe (
                "Kills #1, #123\n"
                        + "what\n"
                        + "killed #25\n"
                        + "handle #33, Closes #100, Handled #3\n"
                        + "other")
    }

    @Test
    fun `should parse properly if important notes comes after references`() {
        val commit = customParser.parse(
            "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\n"
                    + "Kills #1, #123\n"
                    + "BREAKING AMEND: some breaking change\n"
        )

        commit.notes[0] shouldBe
                CommitNote(
                    title = "BREAKING AMEND",
                    text = "some breaking change"
                )
        commit.references shouldBe listOf(
            CommitReference(
                action = "Kills",
                owner = null,
                repository = null,
                issue = "1",
                raw = "#1",
                prefix = "#"
            ),
            CommitReference(
                action = "Kills",
                owner = null,
                repository = null,
                issue = "123",
                raw = ", #123",
                prefix = "#"
            )
        )
        commit.footer shouldBe "Kills #1, #123\nBREAKING AMEND: some breaking change"
    }


    // it('should parse properly if important notes comes with more than one paragraphs after references', () => {
    //        const commit = customParser.parse(
    //          'feat(scope): broadcast $destroy event on scope destruction\n'
    //          + 'perf testing shows that in chrome this change adds 5-15% overhead\n'
    //          + 'when destroying 10k nested scopes where each scope has a $destroy listener\n'
    //          + 'Kills #1, #123\n'
    //          + 'BREAKING AMEND: some breaking change\nsome other breaking change'
    //        )
    //
    //        expect(commit.notes[0]).toEqual({
    //          title: 'BREAKING AMEND',
    //          text: 'some breaking change\nsome other breaking change'
    //        })
    //        expect(commit.references).toEqual([
    //          {
    //            action: 'Kills',
    //            owner: null,
    //            repository: null,
    //            issue: '1',
    //            raw: '#1',
    //            prefix: '#'
    //          },
    //          {
    //            action: 'Kills',
    //            owner: null,
    //            repository: null,
    //            issue: '123',
    //            raw: ', #123',
    //            prefix: '#'
    //          }
    //        ])
    //        expect(commit.footer).toBe('Kills #1, #123\nBREAKING AMEND: some breaking change\nsome other breaking change')
    //      })
    @Test
    fun `should parse properly if important notes comes with more than one paragraphs after references`() {
        val commit = customParser.parse(
            "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\n"
                    + "Kills #1, #123\n"
                    + "BREAKING AMEND:\n"
                    + "some breaking change\n"
                    + "some other breaking change"
        )

        commit.notes[0] shouldBe
                CommitNote(
                    title = "BREAKING AMEND",
                    text = "some breaking change\nsome other breaking change"
                )
        commit.references shouldBe listOf(
            CommitReference(
                action = "Kills",
                owner = null,
                repository = null,
                issue = "1",
                raw = "#1",
                prefix = "#"
            ),
            CommitReference(
                action = "Kills",
                owner = null,
                repository = null,
                issue = "123",
                raw = ", #123",
                prefix = "#"
            )
        )
        commit.footer shouldBe "Kills #1, #123\nBREAKING AMEND:\nsome breaking change\nsome other breaking change"
    }


    @Test
    fun `should add the subject as note if it match breakingHeaderPattern`() {
        val parser = CommitParser(
            ParserOptions(
                headerPattern = Regex("^(\\w*)(?:\\(([\\w$.*\\- ]*)\\))?: (.*)$"),
                breakingHeaderPattern = Regex("^(\\w*)(?:\\((.*)\\))?!: (.*)$"),
                headerCorrespondence = listOf(
                    "type",
                    "scope",
                    "subject"
                )
            )
        )
        val commit = parser.parse(
            "feat!: breaking change feature"
        )

        commit.notes[0] shouldBe
                CommitNote(
                    title = "BREAKING CHANGE",
                    text = "breaking change feature"
                )
    }

    @Test
    fun `should not duplicate notes if the subject match breakingHeaderPattern`() {
        val parser = CommitParser(
            ParserOptions(
                headerPattern = Regex("^(\\w*)(?:\\(([\\w$.*\\- ]*)\\))?: (.*)$"),
                breakingHeaderPattern = Regex("^(\\w*)(?:\\((.*)\\))?!: (.*)$"),
                headerCorrespondence = listOf(
                    "type",
                    "scope",
                    "subject"
                ),
                noteKeywords = listOf("BREAKING AMEND")
            )
        )
        val commit = parser.parse(
            "feat!: breaking change feature\nBREAKING AMEND: some breaking change"
        )

        commit.notes[0] shouldBe
                CommitNote(
                    title = "BREAKING AMEND",
                    text = "some breaking change"
                )
        commit.notes.size shouldBe 1
    }

}
