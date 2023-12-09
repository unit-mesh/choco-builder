package cc.unitmesh.prompt.executor

import cc.unitmesh.prompt.model.Job
import cc.unitmesh.prompt.model.JobStrategy
import cc.unitmesh.prompt.model.PromptScript
import cc.unitmesh.prompt.model.Variable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.math.BigDecimal
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
            strategy.value.forEach { variable ->
                when (variable) {
                    is Variable.KeyValue -> TODO()
                    is Variable.Range -> {
                        runRangeJob(variable) { value ->
                            val temperature: BigDecimal? = if (variable.key == "temperature") value else null
                            log.info("execute job: $jobName, strategy: ${strategy.value}, temperature: $temperature")
                            RepeatExecuteStrategy(jobName, job, basePath).apply {
                                val llmResult = execSingleJob(jobName, job, temperature)
                                handleJobResult(jobName, job, llmResult)
                            }
                        }
                    }
                }
            }
        }

        is JobStrategy.Repeat -> {
            repeat(strategy.value) { index ->
                log.info("execute job: $jobName, strategy: repeat, times: ${index}/${strategy.value}")
                RepeatExecuteStrategy(jobName, job, basePath).apply {
                    val llmResult = execSingleJob(jobName, job)
                    handleJobResult(jobName, job, llmResult)
                }
            }
        }

        is JobStrategy.DatasourceCollection -> {
            DatasourceCollectionStrategy(job, basePath, jobName, strategy).execute()
        }
    }

    private fun runRangeJob(variable: Variable.Range, function: (value: BigDecimal) -> Unit) {
        val closedRange: ClosedRange<BigDecimal> = variable.toRange()
        val step = variable.step.toBigDecimal()

        var currentValue = closedRange.start
        while (currentValue <= closedRange.endInclusive) {
            function(currentValue)
            currentValue += step
        }
    }
}
