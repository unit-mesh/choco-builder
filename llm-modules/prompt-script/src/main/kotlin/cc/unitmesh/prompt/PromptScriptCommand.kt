package cc.unitmesh.prompt

import cc.unitmesh.prompt.executor.ScriptExecutor
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required

val HELP_TEXT = """Run custom prompt script for AI Workflow PoC""".trimIndent()

class PromptScriptCommand : CliktCommand(help = HELP_TEXT) {
    /**
     * Input file should be a yaml file.
     */
    val input by option(help = "input file").required()

    companion object {
        val logger = org.slf4j.LoggerFactory.getLogger(PromptScriptCommand::class.java)
    }

    override fun run() {
        // check is yaml file
        if (!input.endsWith(".yaml") && !input.endsWith(".yml")) {
            throw Exception("input file should be a yaml file: $input")
        }

        // check input file exits
        val file = java.io.File(input)
        if (!file.exists()) {
            throw Exception("input file not found: $input")
        }

        // execute script
        val executor = ScriptExecutor(file)
        executor.execute()

        logger.debug("execute script success: $input")
    }
}