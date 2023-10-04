package cc.unitmesh.prompt.validate

/**
 * Validator is an interface for validating result.
 */
interface Validator {
    val input: String
    fun validate(): Boolean
}