package cc.unitmesh.template

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import java.io.File
import java.io.StringWriter


class VelocityCompiler : PromptCompiler {
    private val velocityContext = VelocityContext()

    companion object {
        val logger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(VelocityCompiler::class.java)
    }

    private fun loadJson(fileName: String): JsonObject? {
        // check is json or jsonl file
        if (!fileName.endsWith(".json")) {
            logger.error("unsupported data file: $fileName")
            return null
        }

        return try {
            val fileContent = File(fileName).readText()
            when {
                fileName.endsWith(".json") -> {
                    JsonParser.parseString(fileContent)?.getAsJsonObject()
                }

                else -> {
                    logger.error("unsupported data file: $fileName")
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun append(key: String, value: Any) {
        velocityContext.put(key, value)
    }

    override fun compile(templatePath: String, dataPath: String): String {
        val obj = loadJson(dataPath)!!
        obj.asMap().forEach { (key, u) ->
            velocityContext.put(key, u)
        }

        val template = File(templatePath).readText()
        return compileToString(template)
    }

    override fun compile(templatePath: String, element: JsonElement): String {
        val obj = element.asJsonObject
        obj.asMap().forEach { (key, u) ->
            velocityContext.put(key, u)
        }

        val template = File(templatePath).readText()
        return compileToString(template)
    }

    /**
     * Compiles a template using Velocity and returns the compiled result as a string.
     *
     * @param templatePath The path to the template file.
     * @param data A map containing the data to be used in the template. The keys in the map represent the variable names
     * and the values represent the corresponding values to be used in the template.
     * @return The compiled template as a string.
     */
    override fun compile(templatePath: String, data: Map<String, Any>): String {
        data.forEach { (key, u) ->
            velocityContext.put(key, u)
        }

        val template = File(templatePath).readText()
        return compileToString(template)
    }

    override fun compileToString(template: String): String {
        val sw = StringWriter()
        Velocity.evaluate(velocityContext, sw, "#" + this.javaClass.name, template)
        return sw.toString()
    }
}