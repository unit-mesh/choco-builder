package cc.unitmesh.prompt.validate

import com.google.gson.JsonParser

class JsonValidator(private val jsonString: String) : Validator {
    override fun validate(): Boolean {
        return try {
            val json = JsonParser.parseString(jsonString)
            true
        } catch (e: Exception) {
            false
        }
    }
}