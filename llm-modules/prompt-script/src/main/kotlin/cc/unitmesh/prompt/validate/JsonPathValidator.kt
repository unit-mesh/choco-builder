package cc.unitmesh.prompt.validate

import com.jayway.jsonpath.JsonPath

/**
 * JsonPath will validate is path is valid. If a path is invalid, will return false.
 */
class JsonPathValidator(val expression: String, override val llmResult: String) : Validator {
    override fun validate(): Boolean = try {
        JsonPath.parse(llmResult).read<Any>(expression)
        true
    } catch (e: Exception) {
        false
    }
}