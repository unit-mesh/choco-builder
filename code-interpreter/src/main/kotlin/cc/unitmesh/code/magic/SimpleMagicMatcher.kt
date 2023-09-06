package cc.unitmesh.code.magic

class SimpleMagicMatcher {
    private val USE_REGEX = Regex("""^\s*%use\s+([\w, ]+)""")

    /**
     * Parse magics from code
     * @param code code to parse
     * @return list of langs, like [spring, ktor, mysql]
     *
     * examples:
     * code = "%use spring" -> return ["spring"]
     * code = "%use spring, mysql" -> return ["spring", "mysql"]
     * code = """%use mysql
     * %use spring
     * """ -> return ["mysql", "spring"]
     */
    fun parseLang(code: String): List<String> {
        val magics = mutableListOf<String>()
        val pattern = USE_REGEX
        for (line in code.lines()) {
            val matchResult = pattern.matchEntire(line.trim())
            if (matchResult != null) {
                val langs = matchResult.groupValues[1].split(",").map { it.trim() }
                magics.addAll(langs)
            }
        }
        return magics
    }
}
