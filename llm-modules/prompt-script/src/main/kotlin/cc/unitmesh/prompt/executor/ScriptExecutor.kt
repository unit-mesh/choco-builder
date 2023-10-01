package cc.unitmesh.prompt.executor

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.cf.core.llms.MockLlmProvider
import cc.unitmesh.connection.BaseConnection
import cc.unitmesh.connection.MockLlmConnection
import cc.unitmesh.connection.OpenAiConnection
import cc.unitmesh.openai.OpenAiProvider
import cc.unitmesh.prompt.model.Job
import cc.unitmesh.prompt.model.PromptScript
import cc.unitmesh.prompt.model.StrategyItem
import cc.unitmesh.prompt.model.Variable
import cc.unitmesh.prompt.template.TemplateCompilerFactory
import cc.unitmesh.prompt.template.TemplateEngineType
import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.datetime.*
import kotlinx.serialization.decodeFromString
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Path

class ScriptExecutor {
    private val content: String
    private var basePath: Path = Path.of(".")

    companion object {
        val log = LoggerFactory.getLogger(ScriptExecutor::class.java)
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

    private fun execStrategy(it: StrategyItem, name: String, job: Job) = when (it) {
        is StrategyItem.ConnectionItem -> {
            it.value.forEach { variable ->
                when (variable) {
                    is Variable.KeyValue -> {
                        TODO()
                    }

                    is Variable.Range -> {
                        runRangeJob(variable) { value ->
                            val llmResult = execSingleJob(name, job)
                            handleSingleJobResult(name, job, llmResult)
                        }
                    }
                }
            }
        }

        is StrategyItem.RepeatItem -> {
            repeat(it.value) {
                val llmResult = execSingleJob(name, job)
                handleSingleJobResult(name, job, llmResult)
            }
        }
    }

    private fun runRangeJob(variable: Variable.Range, function: (value: Double) -> Unit) {
        val closedRange: ClosedRange<Double> = variable.toRange()
        val step = variable.step.toDouble()

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
        val resultFileName = createFileName(name, job)

        val resultFile = this.basePath.resolve(resultFileName).toFile()
        val relativePath = this.basePath.relativize(resultFile.toPath())
        log.info("write result to file: $relativePath")
        resultFile.writeText(llmResult)
    }

    private fun execSingleJob(name: String, job: Job): String {
        val connection = initConnectionConfig(job)
        val llmProvider = when (connection) {
            is OpenAiConnection -> OpenAiProvider(connection.apiKey, connection.apiHost)
            is MockLlmConnection -> MockLlmProvider()
            else -> throw Exception("unsupported connection type: ${connection.type}")
        }

        val prompt = createTemplate(job)
        val msgs = TemplateRoleSplitter().split(prompt)
        val messages = toMessages(msgs)

        if (messages.isEmpty()) {
            throw Exception("no messages found in template")
        }

        return llmProvider.completion(messages)
    }

    private fun createFileName(name: String, job: Job): String {
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
                val factory = TemplateCompilerFactory(type = TemplateEngineType.VELOCITY)
                val templatePath = this.basePath.resolve(job.template).toString()
                factory.compile(templatePath, job.templateDatasource, this.basePath)
            }

            else -> throw Exception("unsupported template type: $ext")
        }
    }

    private fun initConnectionConfig(job: Job): BaseConnection {
        val connectionFile = this.basePath.resolve(job.connection).toFile()
        log.info("connection file: ${connectionFile.absolutePath}")
        val text = connectionFile.readBytes().toString(Charsets.UTF_8)

        val configuration = YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property)
        val connection = Yaml(configuration = configuration).decodeFromString<BaseConnection>(text)
        return connection.convert()
    }
}

private fun toMessages(msgs: Map<String, String>): List<LlmMsg.ChatMessage> {
    return msgs.map {
        LlmMsg.ChatMessage(
            role = LlmMsg.ChatRole.from(it.key),
            content = it.value,
        )
    }
}
