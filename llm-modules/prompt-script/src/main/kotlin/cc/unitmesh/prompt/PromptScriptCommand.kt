package cc.unitmesh.prompt

import cc.unitmesh.prompt.executor.ScriptExecutor
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import org.slf4j.Logger
import java.io.File

val HELP_TEXT = """Run custom prompt script for AI Workflow PoC""".trimIndent()

class PromptScriptCommand : CliktCommand(help = HELP_TEXT) {
    /**
     * Input file should be a yaml file.
     */
    val config by option(help = "prompt script config file").required()

    companion object {
        val logger: Logger = org.slf4j.LoggerFactory.getLogger(PromptScriptCommand::class.java)
    }

    override fun run() {
        // check is yaml file
        if (!config.endsWith(".yaml") && !config.endsWith(".yml")) {
            throw Exception("input file should be a yaml file: $config")
        }

        // check input file exits
        val file = File(config)
        if (!file.exists()) {
            throw Exception("input file not found: $config")
        }

        // execute script
        val executor = ScriptExecutor(file)
        executor.execute()

        logger.debug("execute script success: $config")
    }
}