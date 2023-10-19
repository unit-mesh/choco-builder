package cc.unitmesh.template

interface PromptCompiler {
    /**
     * Appends a key-value pair to the existing data.
     *
     * @param key The key to append.
     * @param value The value to append.
     */
    fun append(key: String, value: Any)
    fun compile(templatePath: String, dataPath: String): String
    fun compile(templatePath: String, data: Map<String, Any>): String

    /**
     * Compiles the given template into a string representation.
     *
     * @param template the path of the template to be compiled
     * @return the compiled template as a string
     */
    fun compileToString(template: String): String
}

