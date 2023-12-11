package cc.unitmesh.prompt.executor.strategy

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.prompt.executor.ScriptExecutor
import cc.unitmesh.prompt.executor.base.JobStrategyExecutor
import cc.unitmesh.prompt.model.Job
import cc.unitmesh.prompt.model.JobStrategy
import cc.unitmesh.prompt.model.TemplateDatasource
import cc.unitmesh.prompt.template.TemplateDataCompile
import cc.unitmesh.template.TemplateEngineType
import cc.unitmesh.template.TemplateRoleSplitter
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.math.BigDecimal
import java.nio.file.Path

class DatasourceCollectionStrategy(
    val job: Job,
    override val basePath: Path,
    private val jobName: String,
    private val collection: JobStrategy.DatasourceCollection,
) : JobStrategyExecutor {
    companion object {
        val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(DatasourceCollectionStrategy::class.java)
    }

    override fun execute() {
        val data: JsonArray = loadCollection(job.templateDatasource)
        data.forEach { item ->
            val collection: List<Map<String, String>> = collection.value
            var temperature: BigDecimal? = null
            collection.forEach { variable ->
                if (variable.containsKey("temperature")) {
                    temperature = variable["temperature"]?.toBigDecimal()
                }
            }

            val llmResult = execJob(job, item, temperature)
            handleJobResult(jobName, job, llmResult, item)
        }
    }

    fun execJob(job: Job, item: JsonElement, temperature: BigDecimal? = null): String {
        val llmProvider = createLlmProvider(job, temperature)

        val ext = job.template.substringAfterLast(".")
        val prompt = when (ext) {
            "vm", "vsl", "ft" -> {
                val factory = TemplateDataCompile(type = TemplateEngineType.VELOCITY)
                val templatePath = basePath.resolve(job.template).toString()
                factory.compile(templatePath, item)
            }

            else -> throw Exception("unsupported template type: $ext")
        }

        val msgs = TemplateRoleSplitter().split(prompt)
        val messages = LlmMsg.fromMap(msgs)

        if (messages.isEmpty()) {
            throw Exception("no messages found in template")
        }

        val resultFileName = createFileName("prompt-log")
        val logbasePath = Path.of(job.logPath)
        if (!logbasePath.toFile().exists()) {
            logbasePath.toFile().mkdirs()
        }

        val resultFilePath = logbasePath.resolve(resultFileName)
        writeToFile(resultFilePath.toString(), messages.joinToString("\n") { it.content })

        log.info("save prompt to debug file: $resultFilePath")
        return llmProvider.completion(messages)
    }

    private fun loadCollection(sources: List<TemplateDatasource>): JsonArray {
        val results = JsonArray()

        sources.forEach { datasource ->
            when (datasource) {
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
                            ScriptExecutor.log.error("unsupported datasource file: ${file.absolutePath}")
                        }
                    }
                }
            }
        }

        return results
    }
}
