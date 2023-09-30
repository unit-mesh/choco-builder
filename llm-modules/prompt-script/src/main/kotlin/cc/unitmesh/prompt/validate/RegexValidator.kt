package cc.unitmesh.prompt.validate

import java.util.regex.Pattern

class RegexValidator(val regex: String, override val input: String) : Validator {
    override fun validate(): Boolean {
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(input)
        return matcher.matches()
    }
}