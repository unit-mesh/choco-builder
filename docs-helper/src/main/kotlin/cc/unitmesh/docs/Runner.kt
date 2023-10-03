package cc.unitmesh.docs

import com.github.ajalt.clikt.core.CliktCommand

class Runner : CliktCommand() {
    override fun run() {
        PromptScriptDocGen().execute()
    }
}

fun main(args: Array<String>) = Runner().main(args)

