package cc.unitmesh.prompt.executor.strategy

import cc.unitmesh.prompt.executor.base.SingleJobExecuteStrategy
import cc.unitmesh.prompt.model.Job
import cc.unitmesh.prompt.model.JobStrategy
import cc.unitmesh.prompt.model.Variable
import com.google.gson.JsonObject
import java.math.BigDecimal
import java.nio.file.Path

class ConnectionExecuteStrategy(
    override val jobName: String,
    override val job: Job,
    override val basePath: Path,
    private val strategy: JobStrategy.Connection,
) : SingleJobExecuteStrategy(jobName, job, basePath) {
    override fun execute() {
        strategy.value.forEach { variable ->
            when (variable) {
                is Variable.KeyValue -> TODO()
                is Variable.Range -> {
                    runRangeJob(variable) { value ->
                        val temperature: BigDecimal? = if (variable.key == "temperature") value else null
                        log.info("execute job: $jobName, strategy: ${strategy.value}, temperature: $temperature")

                        val llmResult = execSingleJob(jobName, job, temperature)
                        handleJobResult(jobName, job, llmResult, JsonObject())
                    }
                }
            }
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
