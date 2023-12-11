package cc.unitmesh.prompt.validate

import java.util.regex.Pattern

/**
 * RegexValidator will validate is input matches regex. If input is invalid, will return false.
 */
class RegexValidator(val regex: String, override val llmResult: String) : Validator {
    override fun validate(): Boolean {
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(llmResult)
        return matcher.matches()
    }
}