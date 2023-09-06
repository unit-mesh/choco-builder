package cc.unitmesh.cf.core.base

private val ASK_REGEX = Regex("(询问|ask)[：:]\\s?(.*)")
private val ACTION_REGEX = Regex("(行动|action)[：:]\\s?(.*)")
private val FINAL_OUTPUT_REGEX = Regex("(最终输出|output)[：:]\\s?(.*)")

enum class FlowActionFlag {
    CONTINUE,
    FINISH;

    companion object {

        fun parse(content: String): Pair<FlowActionFlag, String> {
            val value = extractContent(ACTION_REGEX, content)
            val action = safeValueOf<FlowActionFlag>(value)
            return when (action) {
                CONTINUE -> {
                    Pair(action, extractContent(ASK_REGEX, content))
                }

                FINISH -> {
                    Pair(action, extractContent(FINAL_OUTPUT_REGEX, content))
                }

                else -> throw RuntimeException("Action parsing failed: $content")
            }
        }

        private fun extractContent(pattern: Regex, content: String) =
            pattern.find(content)?.groupValues?.get(2)
                ?: throw RuntimeException("Content parsing failed: $content")
    }
}

inline fun <reified T : kotlin.Enum<T>> safeValueOf(type: String?): T? {
    return java.lang.Enum.valueOf(T::class.java, type)
}