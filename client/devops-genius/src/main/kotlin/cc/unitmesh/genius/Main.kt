package cc.unitmesh.genius

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands


val HELP_TEXT =
    """GenAI/AIGC in DevOps practices that improve software development and operations through the integration.""".trimIndent()


class GeniusCommand : CliktCommand(help = HELP_TEXT) {
    override fun run() = Unit
}

fun main(args: Array<String>) = GeniusCommand().subcommands(
    CodeReviewCommand(),
    CiCdCommand(),
    IssueCommand(),
).main(args)
