package cc.unitmesh.prompt.executor.strategy

import cc.unitmesh.prompt.executor.base.SingleJobExecuteStrategy
import cc.unitmesh.prompt.model.Job
import cc.unitmesh.prompt.model.JobStrategy
import com.google.gson.JsonObject
import java.nio.file.Path

class RepeatExecuteStrategy(
    override val jobName: String,
    override val job: Job,
    override val basePath: Path,
    private val strategy: JobStrategy.Repeat,
) : SingleJobExecuteStrategy(jobName, job, basePath) {
    override fun execute() {
        repeat(strategy.value) { index ->
            log.info("execute job: $jobName, strategy: repeat, times: ${index}/${strategy.value}")
            val llmResult = execSingleJob(jobName, job)
            handleJobResult(jobName, job, llmResult, JsonObject())
        }
    }
}
