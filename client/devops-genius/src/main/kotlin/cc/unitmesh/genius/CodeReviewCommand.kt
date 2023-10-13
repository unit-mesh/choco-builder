package cc.unitmesh.genius

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

class CodeReviewCommand : CliktCommand(help = "Code Review with AIGC") {
    val verbose by option().flag("--no-verbose")

    override fun run() {

    }
}
