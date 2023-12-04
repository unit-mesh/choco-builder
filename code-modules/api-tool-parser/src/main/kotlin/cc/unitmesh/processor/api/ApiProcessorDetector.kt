package cc.unitmesh.processor.api

import cc.unitmesh.processor.api.base.ApiProcessor
import cc.unitmesh.processor.api.parser.PostmanProcessor
import cc.unitmesh.processor.api.swagger.Swagger3Processor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

object ApiProcessorDetector {
    private val logger: Logger = LoggerFactory.getLogger(ApiProcessorDetector::class.java)

    fun detectApiProcessor(file: File, withPostman: Boolean = false, postmanOnly: Boolean = false): ApiProcessor? {
        val content = file.readText()

        if (withPostman || postmanOnly) {
            if (file.extension == "json") {
                val isPostmanContent = content.contains("_postman_id") && content.contains("schema")
                if (isPostmanContent) {
                    return PostmanProcessor(file)
                }
            }
        }

        if (postmanOnly) {
            return null
        }

        // if not json, yaml, yml file, skip
        if (!file.extension.matches(Regex("json|yaml|yml"))) {
            return null
        }

        return getSwaggerProcessor(file)?.let {
            return it
        }
    }

    private fun getSwaggerProcessor(it: File): ApiProcessor? {
        return try {
            val openAPI = Swagger3Processor.fromFile(it) ?: return null
            Swagger3Processor(openAPI)
        } catch (e: Exception) {
            logger.info("Failed to parse ${it.absolutePath}", e)
            null
        }
    }
}
