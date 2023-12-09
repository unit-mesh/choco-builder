package cc.unitmesh.prompt.executor

import cc.unitmesh.cf.core.llms.LlmMsg
import cc.unitmesh.prompt.model.Job
import cc.unitmesh.prompt.template.TemplateDataCompile
import cc.unitmesh.template.TemplateEngineType
import cc.unitmesh.template.TemplateRoleSplitter
import java.math.BigDecimal
import java.nio.file.Path

class RepeatExecuteStrategy(
    val jobName: String,
    val job: Job,
    override val basePath: Path,
) : JobStrategyExecutor {
    companion object {
        val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(RepeatExecuteStrategy::class.java)
    }

    override fun execute() {
        TODO("Not yet implemented")
    }

    fun execSingleJob(name: String, job: Job, temperature: BigDecimal? = null): String {
        val llmProvider = createLlmProvider(job, temperature)

        val prompt = renderSingleJobTemplate(job)
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

    fun renderSingleJobTemplate(job: Job): String {
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
}