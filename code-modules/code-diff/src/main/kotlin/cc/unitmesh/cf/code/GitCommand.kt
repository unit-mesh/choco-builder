package cc.unitmesh.cf.code

import cc.unitmesh.cf.code.command.Command
import cc.unitmesh.cf.code.command.ExecOptions
import cc.unitmesh.cf.code.command.StringListExecListeners

class GitCommand(var workingDirectory: String = ".") {
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(GitCommand::class.java)
    }

    private val gitEnv: MutableMap<String, String> = mutableMapOf(
        "GIT_TERMINAL_PROMPT" to "0", // Disable git prompt
        "GCM_INTERACTIVE" to "Never" // Disable prompting for git credential manager
    )

    // enable for custom git path
    var gitPath = "git"

    private val exec = Command()

    fun log(format: String? = null): GitOutput {
        val args = if (format != null) {
            listOf("log", "-1", format)
        } else {
            listOf("log", "-1")
        }
        val silent = format != null
        return execGit(args, false, silent)
    }

    fun latestCommitHash(num: Int = 10): GitOutput {
        val format = "--pretty=format:%h"
        val args = listOf("log", "-$num", format)
        return execGit(args, false, silent = true)
    }

    private fun execGit(args: List<String>, allowAllExitCodes: Boolean = false, silent: Boolean = false): GitOutput {
        val result = GitOutput()

        val env = mutableMapOf<String, String>()
        for ((key, value) in System.getenv()) {
            env[key] = value
        }
        for ((key, value) in gitEnv) {
            env[key] = value
        }

        val stdout = mutableListOf<String>()
        val stderr = mutableListOf<String>()
        val options = ExecOptions(
            cwd = workingDirectory,
            env = env,
            silent = silent,
            ignoreReturnCode = allowAllExitCodes,
            listeners = StringListExecListeners(stdout, stderr)
        )

        result.exitCode = exec.exec(gitPath, args, options)
        result.stdout = stdout.joinToString("\n")

        if (result.stdout.isNotEmpty()) {
            logger.info(result.stdout)
        }

        logger.info(stderr.joinToString(""))

        return result
    }
}

data class GitOutput(
    var stdout: String = "",
    var exitCode: Int = 0,
)