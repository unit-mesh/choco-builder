package cc.unitmesh.prompt.validate

import cc.unitmesh.cf.core.parser.MarkdownCode
import com.google.gson.JsonElement
import com.jayway.jsonpath.JsonPath
import org.slf4j.Logger

class CompletionValidator(
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
        val logger: Logger = org.slf4j.LoggerFactory.getLogger(CompletionValidator::class.java)
    }

    override fun validate(): Boolean {
        val afterCursorCode = MarkdownCode.parse(llmResult).text
        val beforeCursorCode: String = JsonPath.parse(dataItem.toString()).read(selection)
        val fullCode = "$beforeCursorCode\n$afterCursorCode"

        when (language) {
            "java" -> {
                //
            }

            else -> {
                logger.error("unsupported language: $language")
                return true
            }
        }

        return true
    }
}
