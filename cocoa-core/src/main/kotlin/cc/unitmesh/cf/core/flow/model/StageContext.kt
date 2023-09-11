package cc.unitmesh.cf.core.flow.model

import cc.unitmesh.cf.core.prompt.QAExample
import cc.unitmesh.cf.core.prompt.UpdatableExample
import cc.unitmesh.cf.core.prompt.StringTemplate
import cc.unitmesh.cf.core.llms.TemperatureMode
import cc.unitmesh.cf.core.utils.IdUtil
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.Id
import kotlinx.serialization.Serializable

data class StageContext(
    @Id
    val id: String = IdUtil.uuid(),
    val stage: Stage,
    /**
     * 对于一个不支持 system prompt 的 LLM，需要把 prompt 与用户的输入结合到一起。
     */
    val systemPrompt: String,
    /**
     * for example, "Q:", or `"输入："`
     */
    val questionPrefix: String = "",
    val exampleType: ExampleType = ExampleType.NONE,
    val examples: List<QAExample> = listOf(),
    val updatableExamples: List<UpdatableExample> = listOf(),
    /**
     * when FINISH, isDone is true
     */
    val isDone: Boolean = false,
    /**
     * support for multi-temperature, like "Creative" and "Balanced"
     */
    val temperatures: List<TemperatureMode> = listOf(TemperatureMode.Default),
) : StringTemplate {
    /**
     * 范例类型
     * NONE: 没有范例
     * ONE_CHAT: 在一次聊天里，包含所有的范例
     * MULTI_CHAT: 范例使用多轮聊天的方式
     */
    enum class ExampleType {
        NONE,
        ONE_CHAT,
        MULTI_CHAT,
    }

    @Serializable
    enum class Stage(@JsonValue val value: String) {
        // 归类
        Classify("Classify"),

        // 澄清
        Clarify("Clarify"),

        // 分析
        Analyze("Analyze"),

        // 设计
        Design("Design"),

        // 评审
        Review("Review"),

        // 执行
        Execute("Execute"),

        Done("Done"),

        Custom("Custom")
    }

    override fun format(): String {
        var output = systemPrompt + "\n"
        output += examples.joinToString("\n") {
            "Q:${it.question}\nA:\n${it.answer}\n"
        }

        if (updatableExamples.isNotEmpty()) {
            updatableExamples.forEachIndexed { index, updatableExample ->
                output += "Example $index:\n"
                output += "Q:${updatableExample.question}\nA:\n${updatableExample.answer}\n"
                if (updatableExample.userResponse.isNotEmpty()) {
                    output += "response:${updatableExample.userResponse}\nOutput:${updatableExample.finalOutput}\n"
                }
            }
        }

        return output
    }
}

