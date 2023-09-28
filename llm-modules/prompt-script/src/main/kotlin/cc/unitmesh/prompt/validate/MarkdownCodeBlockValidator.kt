package cc.unitmesh.prompt.validate

import cc.unitmesh.cf.core.parser.MarkdownCode

class MarkdownCodeBlockValidator(override val input: String) : Validator {
    override fun validate(): Boolean {
        val input = MarkdownCode.parse(input)
        return input.text.isNotBlank()
    }
}