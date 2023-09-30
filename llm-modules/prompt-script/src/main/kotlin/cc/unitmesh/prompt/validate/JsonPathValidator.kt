package cc.unitmesh.prompt.validate

import com.jayway.jsonpath.JsonPath

/**
 * JsonPath will validate is path is valid.If path is invalid, will return false.
 */
class JsonPathValidator(val expression: String, override val input: String) : Validator {
    override fun validate(): Boolean = try {
        JsonPath.parse(input).read<Any>(expression)
        true
    } catch (e: Exception) {
        false
    }
}