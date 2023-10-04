package cc.unitmesh.docs

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import java.nio.file.Path

class Runner : CliktCommand() {
    val dir by option("-d", "--dir", help = "The directory to process").default("..")

    override fun run() {
        val rootDir = Path.of(dir).toAbsolutePath().normalize()

        // the prompt script parts
        val promptScriptDir = rootDir.resolve("llm-modules/prompt-script")
        PromptScriptDocGen(promptScriptDir).execute()
    }
}

fun main(args: Array<String>) = Runner().main(args)

