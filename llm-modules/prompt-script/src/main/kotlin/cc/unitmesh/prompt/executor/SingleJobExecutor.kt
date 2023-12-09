package cc.unitmesh.prompt.executor

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.cf.core.llms.MockLlmProvider
import cc.unitmesh.connection.ConnectionConfig
import cc.unitmesh.connection.MockLlmConnection
import cc.unitmesh.connection.OpenAiConnection
import cc.unitmesh.openai.OpenAiProvider
import cc.unitmesh.prompt.model.Job
import cc.unitmesh.prompt.template.TemplateDataCompile
import cc.unitmesh.template.TemplateEngineType
import cc.unitmesh.template.TemplateRoleSplitter
import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.datetime.*
import kotlinx.serialization.decodeFromString
import java.math.BigDecimal
import java.nio.file.Path

class SingleJobExecutor(
    val jobName: String,
    val job: Job,
    private val basePath: Path = Path.of(".")
) {
    companion object {
        val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(SingleJobExecutor::class.java)
    }

    fun execSingleJob(name: String, job: Job, temperature: BigDecimal? = null): String {
        val connection = initConnectionConfig(job)
        val llmProvider = when (connection) {
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

        val prompt = renderTemplate(job)
        val msgs = TemplateRoleSplitter().split(prompt)
        val messages = LlmMsg.fromMap(msgs)

        if (messages.isEmpty()) {
            throw Exception("no messages found in template")
        }

        val resultFileName = createFileName("prompt-log")
        writeToFile(resultFileName, messages.joinToString("\n") { it.content })
        log.info("save prompt to debug file: $resultFileName")

        return llmProvider.completion(messages)
    }

    fun handleSingleJobResult(jobName: String, job: Job, llmResult: String) {
        log.debug("execute job: $jobName")
        val validators = job.buildValidators(llmResult)
        validators.forEach {
            val isSuccess = it.validate()
            val simpleName = it.javaClass.simpleName
            if (!isSuccess) {
                log.error("$simpleName validate failed: ${it.input}")
            } else {
                log.debug("$simpleName validate success: ${it.input}")
            }
        }

        // write to output
        val resultFileName = createFileName(jobName)
        writeToFile(resultFileName, llmResult)
    }

    private fun writeToFile(resultFileName: String, llmResult: String) {
        val resultFile = this.basePath.resolve(resultFileName).toFile()
        val relativePath = this.basePath.relativize(resultFile.toPath())
        log.info("write result to file: $relativePath")
        resultFile.writeText(llmResult)
    }

    private fun createFileName(name: String): String {
        val currentMoment: Instant = Clock.System.now()
        val datetimeInUtc: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.UTC)
        val timeStr = datetimeInUtc.toString().replace(":", "-")
        val jobName = name.replace(" ", "-")

        return "${jobName}-${timeStr}.txt"
    }

    private fun renderTemplate(job: Job): String {
        val ext = job.template.substringAfterLast(".")
        return when (ext) {
            "vm", "vsl", "ft" -> {
                val factory = TemplateDataCompile(type = TemplateEngineType.VELOCITY)
                val templatePath = this.basePath.resolve(job.template).toString()
                factory.compile(templatePath, job.templateDatasource, this.basePath)
            }

            else -> throw Exception("unsupported template type: $ext")
        }
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