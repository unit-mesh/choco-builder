package org.changelog

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class CommentMentionsCommitParserTest {
    @Test
    fun `should ignore comments according to commentChar`() {
        val parser = CommitParser(
            ParserOptions(
                commentChar = "#"
            )
        )

        parser.parse("# comment") shouldBe Commit(
            merge = null,
            header = null,
            body = null,
            footer = null,
            notes = mutableListOf(),
            references = mutableListOf(),
            mentions = mutableListOf(),
            revert = null
        )

        parser.parse(" # non-comment") shouldBe Commit(
            merge = null,
            header = " # non-comment",
            body = null,
            footer = null,
            notes = mutableListOf(),
            references = mutableListOf(),
            mentions = mutableListOf(),
            revert = null
        )

        parser.parse("header\n# comment\n\nbody") shouldBe Commit(
            merge = null,
            header = "header",
            body = "body",
            footer = null,
            notes = mutableListOf(),
            references = mutableListOf(),
            mentions = mutableListOf(),
            revert = null
        )

    }

    // it('should respect commentChar config', () => {
    //        const parser = new CommitParser({
    //          ...customOptions,
    //          commentChar: '*'
    //        })
    //
    //        expect(parser.parse('* comment')).toEqual({
    //          merge: null,
    //          header: null,
    //          body: null,
    //          footer: null,
    //          notes: [],
    //          references: [],
    //          mentions: [],
    //          revert: null
    //        })
    //
    //        expect(parser.parse('# non-comment')).toEqual({
    //          merge: null,
    //          header: '# non-comment',
    //          body: null,
    //          footer: null,
    //          notes: [],
    //          references: [],
    //          mentions: [],
    //          revert: null
    //        })
    //
    //        expect(parser.parse(' * non-comment')).toEqual({
    //          merge: null,
    //          header: ' * non-comment',
    //          body: null,
    //          footer: null,
    //          notes: [],
    //          references: [],
    //          mentions: [],
    //          revert: null
    //        })
    //
    //        expect(parser.parse('header\n* comment\n\nbody')).toEqual({
    //          merge: null,
    //          header: 'header',
    //          body: 'body',
    //          footer: null,
    //          notes: [],
    //          references: [],
    //          mentions: [],
    //          revert: null
    //        })
    //      })
    @Test
    fun `should respect commentChar config`() {
        val parser = CommitParser(
            ParserOptions(
                revertPattern = Regex("^Revert\\s\"([\\s\\S]*)\"\\s*This reverts commit (.*)\\.$"),
                revertCorrespondence = listOf("header", "hash"),
                fieldPattern = Regex("^-(.*?)-$"),
                headerPattern = Regex("^(\\w*)(?:\\(([\\w$.*\\- ]*)\\))?: (.*)$"),
                headerCorrespondence = listOf("type", "scope", "subject"),
                noteKeywords = listOf("BREAKING AMEND"),
                issuePrefixes = listOf("#", "gh-"),
                referenceActions = listOf("kill", "kills", "killed", "handle", "handles", "handled"),
                commentChar = "*"
            )
        )

        parser.parse("* comment") shouldBe Commit(
            merge = null,
            header = null,
            body = null,
            footer = null,
            notes = mutableListOf(),
            references = mutableListOf(),
            mentions = mutableListOf(),
            revert = null
        )

        parser.parse("# non-comment") shouldBe Commit(
            merge = null,
            header = "# non-comment",
            body = null,
            footer = null,
            notes = mutableListOf(),
            references = mutableListOf(),
            mentions = mutableListOf(),
            revert = null
        )

        parser.parse(" * non-comment") shouldBe Commit(
            merge = null,
            header = " * non-comment",
            body = null,
            footer = null,
            notes = mutableListOf(),
            references = mutableListOf(),
            mentions = mutableListOf(),
            revert = null
        )
    }

    @Test
    fun `should mention someone in the commit`() {
        val parser = CommitParser(
            ParserOptions(
                headerPattern = Regex("^(\\w*)(?:\\(([\\w$.*\\- ]*)\\))?: (.*)$"),
                headerCorrespondence = listOf("type", "scope", "subject"),
                mergePattern = Regex("^Merge pull request #(\\d+) from (.*)$"),
                mergeCorrespondence = listOf("issueId", "source")
            )
        )
        val commit = parser.parse(
            "@Steve\n"
                    + "@conventional-changelog @someone"
                    + "\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "@this is"
        )

        commit.mentions shouldBe listOf(
            "Steve",
            "conventional-changelog",
            "someone",
            "this"
        )
    }


}