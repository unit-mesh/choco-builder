package cc.unitmesh.prompt.validate

import com.google.gson.JsonElement

class CompletionValidator(
    /**
     * the origin output
     */
    override val input: String,
    /**
     * the code completion is an object, we need to get the selected value by json path.
     */
    val selection: String,
    /**
     * the input data
     */
    val dataItem: JsonElement,
    /**
     * use for choice language compiler
     */
    val language: String
) : Validator {
    override fun validate(): Boolean {
        return true
    }
}