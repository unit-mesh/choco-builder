package cc.unitmesh.prompt.executor.base

import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.core.llms.MockLlmProvider
import cc.unitmesh.connection.ConnectionConfig
import cc.unitmesh.connection.MockLlmConnection
import cc.unitmesh.connection.OpenAiConnection
import cc.unitmesh.openai.OpenAiProvider
import cc.unitmesh.prompt.model.Job
import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.google.gson.JsonElement
import kotlinx.datetime.*
import kotlinx.serialization.decodeFromString
import java.math.BigDecimal
import java.nio.file.Path

interface JobStrategyExecutor {
    companion object {
        val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(JobStrategyExecutor::class.java)
    }

    val basePath: Path

    fun execute()

    fun createLlmProvider(job: Job, temperature: BigDecimal?): LlmProvider {
        val llmProvider = when (val connection = initConnectionConfig(job)) {
            is OpenAiConnection -> {
                val provider = OpenAiProvider(connection.apiKey, connection.apiHost)
                if (temperature != null) {
                    provider.temperature = temperature.toDouble()
                }
                provider
            }

            is MockLlmConnection -> MockLlmProvider(connection.response)
            else -> throw Exception("unsupported connection type: ${connection.type}")
        }
        return llmProvider
    }

    fun handleJobResult(jobName: String, job: Job, llmResult: String, dataItem: JsonElement) {
        log.debug("execute job: $jobName")
        val validators = job.buildValidators(llmResult, dataItem)
        validators.forEach {
            val isSuccess = it.validate()
            val simpleName = it.javaClass.simpleName
            if (!isSuccess) {
                log.error("$simpleName validate failed: ${it.llmResult}")
            } else {
                log.debug("$simpleName validate success: ${it.llmResult}")
            }
        }

        // write to output
        val resultFileName = createFileName(jobName)
        writeToFile(resultFileName, llmResult)
    }


    fun writeToFile(resultFileName: String, llmResult: String) {
        val resultFile = this.basePath.resolve(resultFileName).toFile()
        val relativePath = this.basePath.relativize(resultFile.toPath())
        // get parent dir and create if not exists
        val parentDir = resultFile.parentFile
        if (!parentDir.exists()) {
            parentDir.mkdirs()
        }

        log.info("write result to file: $relativePath")
        resultFile.writeText(llmResult)
    }

    fun createFileName(name: String): String {
        val currentMoment: Instant = Clock.System.now()
        val datetimeInUtc: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.UTC)
        val timeStr = datetimeInUtc.toString().replace(":", "-")
        val jobName = name.replace(" ", "-")

        return "${jobName}-${timeStr}.txt"
    }

    private fun initConnectionConfig(job: Job): ConnectionConfig {
        val connectionFile = this.basePath.resolve(job.connection).toFile()
        log.info("connection file: ${connectionFile.absolutePath}")
        val text = connectionFile.readBytes().toString(Charsets.UTF_8)

        val configuration = YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property)
        val connection = Yaml(configuration = configuration).decodeFromString<ConnectionConfig>(text)
        return connection.convert()
    }
}