package cc.unitmesh.prompt.validate

import com.google.gson.JsonParser

/**
 * JsonValidator will validate is input is valid json. If input is invalid, will return false.
 */
class JsonValidator(override val llmResult: String) : Validator {
    override fun validate(): Boolean {
        return try {
            JsonParser.parseString(llmResult)
            true
        } catch (e: Exception) {
            false
        }
    }
}