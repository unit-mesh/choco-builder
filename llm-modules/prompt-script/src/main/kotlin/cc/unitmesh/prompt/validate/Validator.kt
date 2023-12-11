package cc.unitmesh.prompt.validate

interface Validator {
    val llmResult: String
    fun validate(): Boolean
}