package cc.unitmesh.rag

/**
 * PromptScript is a DSL for writing a prompt, will generate Markdown text.
 */
class PromptScript {
    val sb = StringBuilder()
    fun codeblock(language: String, function: () -> String): String {
        val sb = StringBuilder()
        sb.append("```")
        sb.append(language)
        sb.append("\n")
        sb.append(function())
        sb.append("\n")
        sb.append("```")
        sb.append("\n")

        this.sb.append(sb)
        return sb.toString()
    }

    fun build(): String {
        return sb.toString()
    }

    fun paragraph(paragraph: String): String {
        this.sb.append(paragraph)
        this.sb.append("\n")
        return paragraph + "\n"
    }

    fun p(paragraph: String): String {
        return paragraph(paragraph)
    }

    fun p(paragraph: () -> String): String {
        return paragraph(paragraph())
    }

    /**
     * type can be unordered, ordered, or checked
     */
    fun list(type: String, function: () -> Unit) {
        function()
    }

    fun item(item: String): String {
        return item
    }
}