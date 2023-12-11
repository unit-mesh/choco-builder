package cc.unitmesh.prompt.validate

import cc.unitmesh.cf.core.parser.MarkdownCode

class MarkdownCodeBlockValidator(override val llmResult: String) : Validator {
    override fun validate(): Boolean {
        val input = MarkdownCode.parse(llmResult)
        return input.text.isNotBlank()
    }
}