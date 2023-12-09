package cc.unitmesh.prompt.executor.strategy

import cc.unitmesh.prompt.executor.ScriptExecutor
import cc.unitmesh.prompt.executor.base.JobStrategyExecutor
import cc.unitmesh.prompt.model.Job
import cc.unitmesh.prompt.model.JobStrategy
import cc.unitmesh.prompt.model.TemplateDatasource
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.nio.file.Path

class DatasourceCollectionStrategy(
    val job: Job,
    override val basePath: Path,
    val jobName: String,
    val collection: JobStrategy.DatasourceCollection,
) : JobStrategyExecutor {
    override fun execute() {
        val data: JsonArray = loadCollection(job.templateDatasource)
        data.forEach { item ->
            val obj = item.asJsonObject
            val temperature = obj.get("temperature")?.asBigDecimal
//            val llmResult = execSingleJob(jobName, job, temperature)
//            handleSingleJobResult(jobName, job, llmResult)
        }
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
