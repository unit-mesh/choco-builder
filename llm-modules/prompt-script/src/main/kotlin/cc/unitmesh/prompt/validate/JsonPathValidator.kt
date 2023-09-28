package cc.unitmesh.prompt.validate

import com.jayway.jsonpath.JsonPath

class JsonPathValidator(val json: String, val path: String) : Validator {
    override fun validate(): Boolean {
        try {
            JsonPath.parse(json).read<Any>(path)
            return true
        } catch (e: Exception) {
            return false
        }
    }
}