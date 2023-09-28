package cc.unitmesh.prompt.validate

import com.jayway.jsonpath.JsonPath

/**
 * JsonPath will validate is path is valid.If path is invalid, will return false.
 */
class JsonPathValidator(val json: String, val path: String) : Validator {
    override fun validate(): Boolean = try {
        JsonPath.parse(json).read<Any>(path)
        true
    } catch (e: Exception) {
        false
    }
}