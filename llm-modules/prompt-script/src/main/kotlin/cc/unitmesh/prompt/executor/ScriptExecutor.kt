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
    private var basePath: Path = Path.of(".")

    companion object {
        val log: Logger = LoggerFactory.getLogger(ScriptExecutor::class.java)
    }

    private constructor(content: String) {
        this.content = content
    }

    constructor(file: File) : this(file.readText(Charsets.UTF_8)) {
        this.basePath = file.toPath().parent
    }

    fun execute() {
        val script: PromptScript = PromptScript.fromString(content) ?: return
        script.jobs.forEach { (name, job) ->
            job.strategy.forEach {
                execStrategy(it, name, job)
            }
        }
    }

    private fun execStrategy(it: JobStrategy, jobName: String, job: Job) = when (it) {
        is JobStrategy.Connection -> {
            it.value.forEach { variable ->
                when (variable) {
                    is Variable.KeyValue -> TODO()
                    is Variable.Range -> {
                        runRangeJob(variable) { value ->
                            val temperature: BigDecimal? = if (variable.key == "temperature") value else null
                            log.info("execute job: $jobName, strategy: ${it.value}, temperature: $temperature")
                            SingleJobExecutor(jobName, job, basePath).apply {
                                val llmResult = execSingleJob(jobName, job, temperature)
                                handleSingleJobResult(jobName, job, llmResult)
                            }
                        }
                    }
                }
            }
        }

        is JobStrategy.Repeat -> {
            repeat(it.value) { index ->
                log.info("execute job: $jobName, strategy: repeat, times: ${index}/${it.value}")
                SingleJobExecutor(jobName, job, basePath).apply {
                    val llmResult = execSingleJob(jobName, job)
                    handleSingleJobResult(jobName, job, llmResult)
                }
            }
        }

        is JobStrategy.DatasourceCollection -> {
            DatasourceCollectionStrategy(job, basePath, jobName, it).execute()
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
