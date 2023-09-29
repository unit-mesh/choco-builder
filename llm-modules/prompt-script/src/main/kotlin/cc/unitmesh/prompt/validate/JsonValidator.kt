package cc.unitmesh.prompt.validate

import com.google.gson.JsonParser

class JsonValidator(override val input: String) : Validator {
    override fun validate(): Boolean {
        return try {
            val json = JsonParser.parseString(input)
            true
        } catch (e: Exception) {
            false
        }
    }
}