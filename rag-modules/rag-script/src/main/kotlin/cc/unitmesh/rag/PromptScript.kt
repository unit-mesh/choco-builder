package cc.unitmesh.rag

/**
 * PromptScript is a DSL for writing a prompt, will generate Markdown text.
 * - codeblock, will generate a codeblock starts with ```language
 * - paragraph, will generate a paragraph
 * - list, will generate a list
 * - linebreak, will generate a linebreak
 */
class PromptScript {
    private val sb = StringBuilder()
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
    fun list(type: ListType, function: () -> List<String>): String {
        val sb = StringBuilder()
        val prefix = when (type) {
            ListType.Unordered -> "* "
            ListType.Ordered -> "1. "
            ListType.Checked -> "- [ ] "
        }

        function().forEach {
            sb.append(prefix)
            sb.append(it)
            sb.append("\n")
        }

        this.sb.append(sb)
        return sb.toString()
    }

    fun linebreak(): String {
        this.sb.append("\n")
        return "\n"
    }

    override fun toString(): String {
        return sb.toString()
    }
}

enum class ListType {
    Unordered,
    Ordered,
    Checked
}