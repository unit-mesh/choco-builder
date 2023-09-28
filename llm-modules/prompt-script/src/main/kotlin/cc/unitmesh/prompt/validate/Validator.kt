package cc.unitmesh.prompt.validate

interface Validator {
    val input: String
    fun validate(): Boolean
}