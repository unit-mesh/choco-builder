package cc.unitmesh.prompt.executor

import cc.unitmesh.prompt.executor.strategy.ConnectionExecuteStrategy
import cc.unitmesh.prompt.executor.strategy.DatasourceCollectionStrategy
import cc.unitmesh.prompt.executor.strategy.RepeatExecuteStrategy
import cc.unitmesh.prompt.model.Job
import cc.unitmesh.prompt.model.JobStrategy
import cc.unitmesh.prompt.model.PromptScript
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Path

class ScriptExecutor {
    private val content: String
    private lateinit var basePath: Path

    companion object {
        val log: Logger = LoggerFactory.getLogger(ScriptExecutor::class.java)
    }

    constructor(file: File) : this(file.readText(Charsets.UTF_8)) {
        this.basePath = file.toPath().parent
    }

    private constructor(content: String) {
        this.content = content
    }


    fun execute() {
        val script: PromptScript = PromptScript.fromString(content) ?: return
        script.jobs.forEach { (name, job) ->
            job.strategy.forEach {
                execStrategy(it, name, job)
            }
        }
    }

    private fun execStrategy(strategy: JobStrategy, jobName: String, job: Job) = when (strategy) {
        is JobStrategy.Connection -> {
            ConnectionExecuteStrategy(jobName, job, basePath, strategy).execute()
        }

        is JobStrategy.Repeat -> {
            RepeatExecuteStrategy(jobName, job, basePath, strategy).execute()
        }

        is JobStrategy.DatasourceCollection -> {
            DatasourceCollectionStrategy(job, basePath, jobName, strategy).execute()
        }
    }
}
