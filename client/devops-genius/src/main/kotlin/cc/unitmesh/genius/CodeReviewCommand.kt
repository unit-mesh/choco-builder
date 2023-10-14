package cc.unitmesh.genius

import cc.unitmesh.cf.code.GitDiffer
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option

class CodeReviewCommand : CliktCommand(help = "Code Review with AIGC") {
    private val repo by option(help = "Git repository path. Use local file path, or Git Url").default(".")
    private val branch by option(help = "Git branch name").default("master")
    private val sinceCommit by option(help = "Begin commit hash").default("HEAD")
    private val untilCommit by option(help = "End commit hash").default("HEAD")

    override fun run() {
        val diff = GitDiffer(repo, branch)
        val callList = diff.countBetween(sinceCommit, untilCommit)
        println("callList: $callList")
    }
}
