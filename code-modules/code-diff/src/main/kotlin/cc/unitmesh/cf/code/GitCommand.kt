package cc.unitmesh.cf.code

import cc.unitmesh.cf.code.command.Command
import cc.unitmesh.cf.code.command.ExecOptions
import cc.unitmesh.cf.code.command.StringListExecListeners

class GitCommand(private var workingDirectory: String = ".") {
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(GitCommand::class.java)
    }

    private val gitEnv: MutableMap<String, String> = mutableMapOf(
        "GIT_TERMINAL_PROMPT" to "0", // Disable git prompt
        "GCM_INTERACTIVE" to "Never" // Disable prompting for git credential manager
    )

    private var gitPath = "git"

    private val exec = Command()

    /**
     * Checks out a specific reference in the Git repository.
     *
     * @param ref the reference to check out
     * @param startPoint the starting point for the new branch (optional)
     * @return the output of the Git command
     */
    fun checkout(ref: String, startPoint: String? = null): GitOutput {
        val args = mutableListOf("checkout", "--progress", "--force")

        if (startPoint != null) {
            args.addAll(listOf("-B", ref, startPoint))
        } else {
            args.add(ref)
        }

        return execGit(args)
    }

    /**
     * Logs the latest commit in the Git repository.
     *
     * @param format the format of the log message (optional)
     * @return the GitOutput object containing the log information
     */
    fun log(format: String? = null): GitOutput {
        val args = if (format != null) {
            listOf("log", "-1", format)
        } else {
            listOf("log", "-1")
        }
        val silent = format != null
        return execGit(args, false, silent)
    }

    /**
     * get the latest commit hash for the current branch
     * @param num the number of commits to return
     */
    fun latestCommitHash(num: Int = 10): GitOutput {
        val format = "--pretty=format:%h"
        val args = listOf("log", "-$num", format)
        return execGit(args, false, silent = true)
    }

    /**
     * Executes a Git command with the given arguments.
     *
     * @param args the list of arguments for the Git command
     * @param allowAllExitCodes flag indicating whether to allow all exit codes or not (default: false)
     * @param silent flag indicating whether to suppress the command output or not (default: false)
     * @return the GitOutput object containing the result of the Git command execution
     */
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