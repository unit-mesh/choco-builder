package cc.unitmesh.prompt.executor

import cc.unitmesh.prompt.model.Job
import cc.unitmesh.prompt.model.PromptScript
import java.io.InputStream

class ScriptExecutor(
    val scriptFile: InputStream
) {
    fun execute() {
        // load script file and parse to PromptScript
        val context = scriptFile.readBytes().toString(Charsets.UTF_8)
        val script: PromptScript = PromptScript.fromString(context) ?: return

        // execute script
        script.jobs.forEach { (name, job) ->
            println("execute job: $name")
            runJob(job)
        }
    }

    private fun runJob(job: Job) {

    }
}