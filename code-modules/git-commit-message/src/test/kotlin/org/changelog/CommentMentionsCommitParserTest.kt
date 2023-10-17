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

    val genericStyleParser = CommitParser(
        ParserOptions(
            headerPattern = Regex("^(\\w*)(?:\\(([\\w$.*\\- ]*)\\))?: (.*)$"),
            headerCorrespondence = listOf("type", "scope", "subject"),
            mergePattern = Regex("^Merge branch '(\\w+)'$"),
            mergeCorrespondence = listOf("source", "issueId")
        )
    )

    @Test
    fun `should parse merge header in merge commit`() {
        val commit = genericStyleParser.parse("Merge branch 'feature'\nHEADER")

        commit.meta["source"] shouldBe "feature"
        commit.meta["issueId"] shouldBe null
    }

    @Test
    fun `should not throw if merge commit has no header`() {
        genericStyleParser.parse("Merge branch 'feature'")
    }

    @Test
    fun `should parse merge as null if options_mergePattern is not defined`() {
        val commit = CommitParser(ParserOptions.defaultOptions()).parse("Merge branch 'feature'\nHEADER")

        commit.merge shouldBe null
    }

    @Test
    fun `should not parse conventional header if pull request header present and mergePattern is not set`() {
        val commit = genericStyleParser.parse(
            "Merge pull request #1 from user/feature/feature-name\n"
                    + "feat(scope): broadcast \$destroy event on scope destruction"
        )

        commit.meta["type"] shouldBe null
        commit.meta["scope"] shouldBe null
        commit.meta["subject"] shouldBe null
    }

    //       describe('github style', () => {
    //        const parser = new CommitParser({
    //          headerPattern: /^(\w*)(?:\(([\w$.\-* ]*)\))?: (.*)$/,
    //          headerCorrespondence: [
    //            'type',
    //            'scope',
    //            'subject'
    //          ],
    //          mergePattern: /^Merge pull request #(\d+) from (.*)$/,
    //          mergeCorrespondence: ['issueId', 'source']
    //        })
    //        const commit = parser.parse(
    //          'Merge pull request #1 from user/feature/feature-name\n'
    //          + '\n'
    //          + 'feat(scope): broadcast $destroy event on scope destruction\n'
    //          + '\n'
    //          + 'perf testing shows that in chrome this change adds 5-15% overhead\n'
    //          + 'when destroying 10k nested scopes where each scope has a $destroy listener'
    //        )
    //
    //        it('should parse header in GitHub like pull request', () => {
    //          expect(commit.header).toBe('feat(scope): broadcast $destroy event on scope destruction')
    //        })
    //
    //        it('should understand header parts in GitHub like pull request', () => {
    //          expect(commit.type).toBe('feat')
    //          expect(commit.scope).toBe('scope')
    //          expect(commit.subject).toBe('broadcast $destroy event on scope destruction')
    //        })
    //
    //        it('should understand merge parts in GitHub like pull request', () => {
    //          expect(commit.merge).toBe('Merge pull request #1 from user/feature/feature-name')
    //          expect(commit.issueId).toBe('1')
    //          expect(commit.source).toBe('user/feature/feature-name')
    //        })
    //
    //        it('should parse header if merge header is missing', () => {
    //          const commit = parser.parse('feat(scope): broadcast $destroy event on scope destruction')
    //
    //          expect(commit.merge).toBe(null)
    //        })
    //      })
    val githubStyleParser = CommitParser(
        ParserOptions(
            headerPattern = Regex("^(\\w*)(?:\\(([\\w$.*\\- ]*)\\))?: (.*)$"),
            headerCorrespondence = listOf("type", "scope", "subject"),
            mergePattern = Regex("^Merge pull request #(\\d+) from (.*)$"),
            mergeCorrespondence = listOf("issueId", "source")
        )
    )

    @Test
    fun `should parse header in GitHub like pull request`() {
        val commit = githubStyleParser.parse(
            "Merge pull request #1 from user/feature/feature-name\n"
                    + "\n"
                    + "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener"
        )

        commit.header shouldBe "feat(scope): broadcast \$destroy event on scope destruction"
    }

    @Test
    fun `should understand header parts in GitHub like pull request`() {
        val commit = githubStyleParser.parse(
            "Merge pull request #1 from user/feature/feature-name\n"
                    + "\n"
                    + "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener"
        )

        commit.meta["type"] shouldBe "feat"
        commit.meta["scope"] shouldBe "scope"
        commit.meta["subject"] shouldBe "broadcast \$destroy event on scope destruction"
    }

    @Test
    fun `should understand merge parts in GitHub like pull request`() {
        val commit = githubStyleParser.parse(
            "Merge pull request #1 from user/feature/feature-name\n"
                    + "\n"
                    + "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener"
        )

        commit.merge shouldBe "Merge pull request #1 from user/feature/feature-name"
        commit.meta["issueId"] shouldBe "1"
        commit.meta["source"] shouldBe "user/feature/feature-name"
    }

    @Test
    fun `should parse header if merge header is missing`() {
        val commit = githubStyleParser.parse("feat(scope): broadcast \$destroy event on scope destruction")

        commit.merge shouldBe null
    }

    //       describe('gitlab style', () => {
    //        const parser = new CommitParser({
    //          headerPattern: /^(\w*)(?:\(([\w$.\-* ]*)\))?: (.*)$/,
    //          headerCorrespondence: [
    //            'type',
    //            'scope',
    //            'subject'
    //          ],
    //          mergePattern: /^Merge branch '([^']+)' into '[^']+'$/,
    //          mergeCorrespondence: ['source']
    //        })
    //        const commit = parser.parse(
    //          'Merge branch \'feature/feature-name\' into \'master\'\r\n'
    //          + '\r\n'
    //          + 'feat(scope): broadcast $destroy event on scope destruction\r\n'
    //          + '\r\n'
    //          + 'perf testing shows that in chrome this change adds 5-15% overhead\r\n'
    //          + 'when destroying 10k nested scopes where each scope has a $destroy listener\r\n'
    //          + '\r\n'
    //          + 'See merge request !1'
    //        )
    //
    //        it('should parse header in GitLab like merge request', () => {
    //          expect(commit.header).toBe('feat(scope): broadcast $destroy event on scope destruction')
    //        })
    //
    //        it('should understand header parts in GitLab like merge request', () => {
    //          expect(commit.type).toBe('feat')
    //          expect(commit.scope).toBe('scope')
    //          expect(commit.subject).toBe('broadcast $destroy event on scope destruction')
    //        })
    //
    //        it('should understand merge parts in GitLab like merge request', () => {
    //          expect(commit.merge).toBe('Merge branch \'feature/feature-name\' into \'master\'')
    //          expect(commit.source).toBe('feature/feature-name')
    //        })
    //      })
    val gitlabStyleParser = CommitParser(
        ParserOptions(
            headerPattern = Regex("^(\\w*)(?:\\(([\\w$.*\\- ]*)\\))?: (.*)$"),
            headerCorrespondence = listOf("type", "scope", "subject"),
            mergePattern = Regex("^Merge branch '([^']+)' into '[^']+'$"),
            mergeCorrespondence = listOf("source")
        )
    )

    @Test
    fun `should parse header in GitLab like merge request`() {
        val commit = gitlabStyleParser.parse(
            "Merge branch 'feature/feature-name' into 'master'\r\n"
                    + "\r\n"
                    + "feat(scope): broadcast \$destroy event on scope destruction\r\n"
                    + "\r\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\r\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\r\n"
                    + "\r\n"
                    + "See merge request !1"
        )

        commit.header shouldBe "feat(scope): broadcast \$destroy event on scope destruction"
    }

    @Test
    fun `should understand header parts in GitLab like merge request`() {
        val commit = gitlabStyleParser.parse(
            "Merge branch 'feature/feature-name' into 'master'\r\n"
                    + "\r\n"
                    + "feat(scope): broadcast \$destroy event on scope destruction\r\n"
                    + "\r\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\r\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\r\n"
                    + "\r\n"
                    + "See merge request !1"
        )

        commit.meta["type"] shouldBe "feat"
        commit.meta["scope"] shouldBe "scope"
        commit.meta["subject"] shouldBe "broadcast \$destroy event on scope destruction"
    }

    @Test
    fun `should understand merge parts in GitLab like merge request`() {
        val commit = gitlabStyleParser.parse(
            "Merge branch 'feature/feature-name' into 'master'\r\n"
                    + "\r\n"
                    + "feat(scope): broadcast \$destroy event on scope destruction\r\n"
                    + "\r\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\r\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\r\n"
                    + "\r\n"
                    + "See merge request !1"
        )

        commit.merge shouldBe "Merge branch 'feature/feature-name' into 'master'"
        commit.meta["source"] shouldBe "feature/feature-name"
    }

    @Test
    fun `should allow colon in scope`() {
        val commit = CommitParser(
            ParserOptions(
                headerPattern = Regex("^(\\w*)(?:\\(([:\\w$.*\\- ]*)\\))?: (.*)$"),
                headerCorrespondence = listOf("type", "scope", "subject")
            )
        ).parse("feat(ng:list): Allow custom separator")

        commit.meta["scope"] shouldBe "ng:list"
    }

    @Test
    fun `should parse header part as null if not captured`() {
        val commit = CommitParser(ParserOptions.defaultOptions()).parse("header")

        commit.meta["type"] shouldBe null
        commit.meta["scope"] shouldBe null
        commit.meta["subject"] shouldBe null
    }

    @Test
    fun `should parse header`() {
        val commit = CommitParser(ParserOptions.defaultOptions()).parse(
            "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\n"
                    + "BREAKING AMEND: some breaking change\n"
                    + "Kills #1, #123\n"
                    + "killed #25\n"
                    + "handle #33, Closes #100, Handled #3 kills repo#77\n"
                    + "kills stevemao/conventional-commits-parser#1"
        )

        commit.header shouldBe "feat(scope): broadcast \$destroy event on scope destruction"
    }

    @Test
    fun `should understand header parts`() {
        val commit = CommitParser(ParserOptions.defaultOptions()).parse(
            "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\n"
                    + "BREAKING AMEND: some breaking change\n"
                    + "Kills #1, #123\n"
                    + "killed #25\n"
                    + "handle #33, Closes #100, Handled #3 kills repo#77\n"
                    + "kills stevemao/conventional-commits-parser#1"
        )

        commit.meta["type"] shouldBe "feat"
        commit.meta["scope"] shouldBe "scope"
        commit.meta["subject"] shouldBe "broadcast \$destroy event on scope destruction"
    }

    @Test
    fun `should allow correspondence to be changed`() {
        val commit = CommitParser(
            ParserOptions(
                headerPattern = Regex("^(\\w*)(?:\\(([\\w$.*\\- ]*)\\))?: (.*)$"),
                headerCorrespondence = listOf("scope", "subject", "type")
            )
        ).parse("scope(my subject): fix this")

        commit.meta["type"] shouldBe "fix this"
        commit.meta["scope"] shouldBe "scope"
        commit.meta["subject"] shouldBe "my subject"
    }

    @Test
    fun `should be undefined if it is missing in options_headerCorrespondence`() {
        val commit = CommitParser(
            ParserOptions(
                headerPattern = Regex("^(\\w*)(?:\\(([\\w$.*\\- ]*)\\))?: (.*)$"),
                headerCorrespondence = listOf("scop", "subject")
            )
        ).parse("scope(my subject): fix this")

        commit.meta["scope"] shouldBe null
    }

    @Test
    fun `should reference an issue with an owner`() {
        val commit = CommitParser(customOptions).parse("handled angular/angular.js#1")

        commit.references shouldBe listOf(
            CommitReference(
                action = "handled",
                issue = "1",
                owner = "angular",
                prefix = "#",
                raw = "angular/angular.js#1",
                repository = "angular.js"
            )
        )
    }

    @Test
    fun `should reference an issue with a repository`() {
        val commit = CommitParser(customOptions).parse("handled angular.js#1")

        commit.references shouldBe listOf(
            CommitReference(
                action = "handled",
                issue = "1",
                owner = null,
                prefix = "#",
                raw = "angular.js#1",
                repository = "angular.js"
            )
        )
    }

    @Test
    fun `should reference an issue without both`() {
        val commit = CommitParser(
            ParserOptions(
                revertPattern = Regex("^Revert\\s\"([\\s\\S]*)\"\\s*This reverts commit (.*)\\.$"),
                revertCorrespondence = listOf("header", "hash"),
                fieldPattern = Regex("^-(.*?)-$"),
                headerPattern = Regex("^(\\w*)(?:\\(([\\w$.*\\- ]*)\\))?: (.*)$"),
                headerCorrespondence = listOf("type", "scope", "subject"),
                noteKeywords = listOf("BREAKING AMEND"),
                issuePrefixes = listOf("#", "gh-")
            )
        ).parse("This is gh-1")

        commit.references shouldBe listOf(
            CommitReference(
                action = null,
                issue = "1",
                owner = null,
                prefix = "gh-",
                raw = "This is gh-1",
                repository = null
            )
        )
    }

    @Test
    fun `should parse body`() {
        val commit = CommitParser(customOptions).parse(
            "feat(scope): broadcast \$destroy event on scope destruction\n"
                    + "perf testing shows that in chrome this change adds 5-15% overhead\n"
                    + "when destroying 10k nested scopes where each scope has a \$destroy listener\n"
                    + "BREAKING AMEND: some breaking change\n"
                    + "Kills #1, #123\n"
                    + "killed #25\n"
                    + "handle #33, Closes #100, Handled #3 kills repo#77\n"
                    + "kills stevemao/conventional-commits-parser#1"
        )

        commit.body shouldBe "perf testing shows that in chrome this change adds 5-15% overhead\nwhen destroying 10k nested scopes where each scope has a \$destroy listener"
    }

    @Test
    fun `should be null if not found`() {
        val commit = CommitParser(customOptions).parse("header")

        commit.body shouldBe null
    }

    @Test
    fun `should parse hash`() {
        val commit = CommitParser(customOptions).parse(
            "My commit message\n"
                    + "-hash-\n"
                    + "9b1aff905b638aa274a5fc8f88662df446d374bd"
        )

        commit.meta["hash"] shouldBe "9b1aff905b638aa274a5fc8f88662df446d374bd"
    }

    @Test
    fun `should parse meta after notes`() {
        val commit = CommitParser(customOptions).parse(
            "build!: first build setup\n"
                    + "\n"
                    + "BREAKING AMEND: New build system.\n"
                    + "\n"
                    + "-hash-\n"
                    + "4937825901dacca88609da354f0e8a8c84ae04ea\n"
                    + "-gitTags-\n"
                    + "\n"
                    + "-committerDate-\n"
                    + "2023-09-16 21:13:23 +0400\n"
        )

        commit.body shouldBe ""
        commit.notes shouldBe listOf(
            CommitNote(
                title = "BREAKING AMEND",
                text = "New build system."
            )
        )
        commit.footer shouldBe "BREAKING AMEND: New build system."
        commit.meta["hash"] shouldBe "4937825901dacca88609da354f0e8a8c84ae04ea"
        commit.meta["gitTags"] shouldBe ""
        commit.meta["committerDate"] shouldBe "2023-09-16 21:13:23 +0400"
    }

    @Test
    fun `should parse sideNotes`() {
        val commit = CommitParser(customOptions).parse(
            "My commit message\n"
                    + "-sideNotes-\n"
                    + "It should warn the correct unfound file names.\n"
                    + "Also it should continue if one file cannot be found.\n"
                    + "Tests are added for these"
        )

        commit.meta["sideNotes"] shouldBe ("It should warn the correct unfound file names.\n"
                + "Also it should continue if one file cannot be found.\n"
                + "Tests are added for these")
    }

    @Test
    fun `should parse committer name and email`() {
        val commit = CommitParser(customOptions).parse(
            "My commit message\n"
                    + "-committerName-\n"
                    + "Steve Mao\n"
                    + "- committerEmail-\n"
                    + "test@github.com"
        )

        commit.meta["committerName"] shouldBe "Steve Mao"
        commit.meta[" committerEmail"] shouldBe "test@github.com"
    }
}