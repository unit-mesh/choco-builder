package cc.unitmesh.prompt.template

import cc.unitmesh.prompt.model.TemplateDatasource


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

    fun compile(templatePath: String, data: Map<String, Any>): String {
        return compiler.compile(templatePath, data)
    }

    fun compile(template: String, templateDatasource: List<TemplateDatasource>): String {
        templateDatasource.forEach {
            when (it) {
                is TemplateDatasource.File -> {
                    return compiler.compile(template, it.value)
                }
            }
        }


        logger.error("unsupported template datasource: $templateDatasource")
        return ""
    }
}