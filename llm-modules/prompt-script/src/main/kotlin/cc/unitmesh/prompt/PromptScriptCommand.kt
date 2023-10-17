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
    val input by option(help = "prompt script config file").required()

    companion object {
        val logger: Logger = org.slf4j.LoggerFactory.getLogger(PromptScriptCommand::class.java)
    }

    override fun run() {
        // check is yaml file
        if (!input.endsWith(".yaml") && !input.endsWith(".yml")) {
            throw Exception("input file should be a yaml file: $input")
        }

        // check input file exits
        executeScript(input)

        logger.debug("execute script success: $input")
    }
}

/**
 * Execute script is a method used for running Kotlin Scripting mods.
 *
 * @param input the path to the input file containing the script to be executed
 * @throws Exception if the input file is not found
 *
 * Example usage:
 * ```kotlin
 * @file:DependsOn("cc.unitmesh:prompt-script:0.3.8")
 *
 * import cc.unitmesh.prompt.executeScript
 *
 * executeScript("config/prompt.unit-mesh.yml")
 * ```
 *
 * This method takes a string input representing the path to the input file containing the script to be executed.
 * It checks if the file exists, and if not, throws an exception indicating that the input file was not found.
 * If the file exists, it creates a ScriptExecutor object with the input file and executes the script using the execute() method of the ScriptExecutor class.
 */
fun executeScript(input: String) {
    val file = File(input)
    if (!file.exists()) {
        throw Exception("input file not found: ${input}")
    }

    // execute script
    val executor = ScriptExecutor(file)
    executor.execute()
}
