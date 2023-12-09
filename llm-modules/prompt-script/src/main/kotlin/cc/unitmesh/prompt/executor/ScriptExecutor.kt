package cc.unitmesh.prompt.executor

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.cf.core.llms.MockLlmProvider
import cc.unitmesh.connection.ConnectionConfig
import cc.unitmesh.connection.MockLlmConnection
import cc.unitmesh.connection.OpenAiConnection
import cc.unitmesh.openai.OpenAiProvider
import cc.unitmesh.prompt.model.*
import cc.unitmesh.prompt.template.TemplateDataCompile
import cc.unitmesh.template.TemplateEngineType
import cc.unitmesh.template.TemplateRoleSplitter
import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.datetime.*
import kotlinx.serialization.decodeFromString
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
        // execute script
        script.jobs.forEach { (name, job) ->
            job.strategy.forEach {
                execStrategy(it, name, job)
            }
        }
    }

    private fun execStrategy(it: JobStrategy, name: String, job: Job) = when (it) {
        is JobStrategy.Connection -> {
            it.value.forEach { variable ->
                when (variable) {
                    is Variable.KeyValue -> TODO()
                    is Variable.Range -> {
                        runRangeJob(variable) { value ->
                            val temperature: BigDecimal? = if (variable.key == "temperature") value else null
                            log.info("execute job: $name, strategy: ${it.value}, temperature: $temperature")
                            val llmResult = execSingleJob(name, job, temperature)
                            handleSingleJobResult(name, job, llmResult)
                        }
                    }
                }
            }
        }

        is JobStrategy.Repeat -> {
            repeat(it.value) { index ->
                log.info("execute job: $name, strategy: repeat, times: ${index}/${it.value}")
                val llmResult = execSingleJob(name, job)
                handleSingleJobResult(name, job, llmResult)
            }
        }

        is JobStrategy.DatasourceCollection -> {
            val data: JsonArray = loadCollection(job.templateDatasource)
            data.forEach { item ->
                val obj = item.asJsonObject
                val temperature = obj.get("temperature")?.asBigDecimal
                val llmResult = execSingleJob(name, job, temperature)
                handleSingleJobResult(name, job, llmResult)
            }
        }
    }

    private fun loadCollection(sources: List<TemplateDatasource>): JsonArray {
        // for now, only support json and jsonl
        val results = JsonArray()
        sources.forEach { datasource ->
            when(datasource) {
               is TemplateDatasource.File -> {
                     val file = this.basePath.resolve(datasource.value).toFile()
                     val text = file.readText(Charsets.UTF_8)
                     val ext = file.extension
                     when (ext) {
                          "json" -> {
                            val obj = JsonParser.parseString(text).asJsonObject
                            results.add(obj)
                          }

                          "jsonl" -> {
                            val lines = text.split("\n")
                            lines.forEach { line ->
                                 val obj = JsonParser.parseString(line).asJsonObject
                                 results.add(obj)
                            }
                          }

                          else -> {
                            log.error("unsupported datasource file: ${file.absolutePath}")
                          }
                     }
               }
           }
        }

        return results
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


    private fun handleSingleJobResult(name: String, job: Job, llmResult: String) {
        log.debug("execute job: $name")
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
        val resultFileName = createFileName(name)
        writeToFile(resultFileName, llmResult)
    }

    private fun writeToFile(resultFileName: String, llmResult: String) {
        val resultFile = this.basePath.resolve(resultFileName).toFile()
        val relativePath = this.basePath.relativize(resultFile.toPath())
        log.info("write result to file: $relativePath")
        resultFile.writeText(llmResult)
    }

    private fun execSingleJob(name: String, job: Job, temperature: BigDecimal? = null): String {
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

        val prompt = createTemplate(job)
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

    private fun createFileName(name: String): String {
        val currentMoment: Instant = Clock.System.now()
        val datetimeInUtc: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.UTC)
        val timeStr = datetimeInUtc.toString().replace(":", "-")
        val jobName = name.replace(" ", "-")

        return "${jobName}-${timeStr}.txt"
    }

    private fun createTemplate(job: Job): String {
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
