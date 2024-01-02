package cc.unitmesh.prompt.validate

import cc.unitmesh.cf.core.parser.MarkdownCode
import chapi.ast.javaast.JavaAnalyser
import chapi.ast.kotlinast.KotlinAnalyser
import com.google.gson.JsonElement
import com.jayway.jsonpath.JsonPath
import org.slf4j.Logger

class CodeCompletionValidator(
    /**
     * the origin output
     */
    override val llmResult: String,
    /**
     * the code completion is an object, we need to get the selected value by json path.
     */
    private val selection: String,
    /**
     * the input data
     */
    private val dataItem: JsonElement,
    /**
     * use for choice language compiler
     */
    private val language: String,
) : Validator {
    companion object {
        val logger: Logger = org.slf4j.LoggerFactory.getLogger(CodeCompletionValidator::class.java)
    }

    override fun validate(): Boolean {
        val afterCursorCode = MarkdownCode.parse(llmResult).text
        val beforeCursorCode: String = JsonPath.parse(dataItem.toString()).read(selection)
        val fullCode = "$beforeCursorCode\n$afterCursorCode"

        when (language) {
            "java" -> {
                val datastructures = try {
                    JavaAnalyser().analysis(fullCode, "test.java").DataStructures
                } catch (e: Exception) {
                    logger.error("java code completion failed: $fullCode")
                    return false
                }

                if (datastructures.isEmpty()) {
                    logger.error("java code completion failed: $fullCode")
                    return false
                }

                return true
            }

            "kotlin" -> {
                val datastructures = try {
                    KotlinAnalyser().analysis(fullCode, "test.kt").DataStructures
                } catch (e: Exception) {
                    logger.error("kotlin code completion failed: $fullCode")
                    return false
                }

                if (datastructures.isEmpty()) {
                    logger.error("kotlin code completion failed: $fullCode")
                    return false
                }

                return true
            }

            else -> {
                logger.error("unsupported language: $language")
                return true
            }
        }

        return true
    }
}
