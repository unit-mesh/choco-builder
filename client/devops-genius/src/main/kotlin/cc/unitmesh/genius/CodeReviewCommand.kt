package cc.unitmesh.genius

import cc.unitmesh.cf.code.GitCommand
import cc.unitmesh.cf.code.GitDiffer
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import org.changelog.CommitParser
import org.changelog.ParserOptions
import java.io.File

class CodeReviewCommand : CliktCommand(help = "Code Review with AIGC") {
    private val repo by option(help = "Git repository path. Use local file path, or Git Url").default(".")
    private val branch by option(help = "Git branch name").default("master")
    private val sinceCommit by option(help = "Begin commit revision").default("")
    private val untilCommit by option(help = "End commit revision. Aka latest").default("")
    private val commitMessageOptionFile by option(help = "commit message option file").default("")

    override fun run() {
        val lastestIds = GitCommand().latestCommitHash(2).stdout.split("\n")
        val sinceCommit = sinceCommit.ifEmpty {
            lastestIds[lastestIds.lastIndex]
        }
        val untilCommit = untilCommit.ifEmpty {
            lastestIds[0]
        }

        val diff = GitDiffer(repo, branch)
        val callList = diff.countBetween(sinceCommit, untilCommit)
        val patch = diff.patchBetween(sinceCommit, untilCommit)
        println("patch: $patch")
//        println("callList: $callList")
    }

    private fun createCommitParser(): CommitParser {
        val parserOptions = if (commitMessageOptionFile.isNotEmpty()) {
            val commitMsgOptionText = File(commitMessageOptionFile).readText()
            ParserOptions.fromString(commitMsgOptionText)
        } else {
            ParserOptions.defaultOptions()
        }

        if (parserOptions == null) {
            throw Exception("commit message option file is not valid: $commitMessageOptionFile")
        }

        return CommitParser(parserOptions)
    }
}
