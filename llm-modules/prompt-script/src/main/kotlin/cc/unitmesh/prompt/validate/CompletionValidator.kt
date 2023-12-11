package cc.unitmesh.prompt.validate

class CompletionValidator(
    /**
     * the JsonObject
     */
    override val input: String,
    /**
     * the code completion is a object, we need to get the selected value by json path.
     */
    val selection: String,
    /**
     * the output of code completion
     */
    val output: String,
    /**
     * use for choice language compiler
     */
    val language: String
) : Validator {
    override fun validate(): Boolean {
        return true
    }
}