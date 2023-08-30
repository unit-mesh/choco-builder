package cc.unitmesh.cf.core.base

enum class ClarificationAction {
    CONTINUE,
    FINISH;

    companion object {
        fun parse(content: String): Pair<ClarificationAction, String> {
            val value = extractContent(Regex("(行动|action)[：:]\\s?(.*)"), content)
            val action = safeValueOf<ClarificationAction>(value)
            return when (action) {
                CONTINUE -> {
                    Pair(CONTINUE, extractContent(Regex("(询问|ask)[：:]\\s?(.*)"), content))
                }

                FINISH -> {
                    Pair(FINISH, extractContent(Regex("(最终输出|output)[：:]\\s?(.*)"), content))
                }

                else -> throw RuntimeException("Action parsing failed.")
            }
        }


        private fun extractContent(pattern: Regex, content: String) =
            pattern.find(content)?.groupValues?.get(2)
                ?: throw RuntimeException("Content parsing failed.")
    }
}

inline fun <reified T : kotlin.Enum<T>> safeValueOf(type: String?): T? {
    return java.lang.Enum.valueOf(T::class.java, type)
}