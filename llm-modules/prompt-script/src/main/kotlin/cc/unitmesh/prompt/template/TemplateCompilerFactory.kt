package cc.unitmesh.prompt.template

import cc.unitmesh.prompt.model.TemplateDatasource
import java.nio.file.Path


class TemplateCompilerFactory(private val type: TemplateEngineType = TemplateEngineType.VELOCITY) {
    companion object {
        val logger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(TemplateCompilerFactory::class.java)
    }

    private val compiler: PromptCompiler = when (type) {
        TemplateEngineType.VELOCITY -> VelocityCompiler()
    }

    fun compile(templatePath: String, dataPath: String): String {
        return compiler.compile(templatePath, dataPath)
    }

    fun compile(template: String, templateDatasource: List<TemplateDatasource>, basePath: Path): String {
        templateDatasource.forEach {
            when (it) {
                is TemplateDatasource.File -> {
                    val dataPath = basePath.resolve(it.value).toString()
                    return compiler.compile(template, dataPath)
                }
            }
        }


        logger.error("unsupported template datasource: $templateDatasource")
        return ""
    }
}