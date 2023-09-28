package cc.unitmesh.prompt.validate

import java.util.regex.Pattern

class RegexValidator(override val input: String, val regex: String) : Validator {
    override fun validate(): Boolean {
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(input)
        return matcher.matches()
    }
}