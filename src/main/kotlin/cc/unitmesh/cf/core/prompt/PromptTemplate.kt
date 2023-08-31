package cc.unitmesh.cf.core.prompt

import cc.unitmesh.cf.infrastructure.utils.uuid
import jakarta.persistence.Id

data class PromptTemplate(
    @Id
    val id: String = uuid(),
    val phase: Phase,
    /**
     * 对于一个不支持 system prompt 的 LLM，需要把 prompt 与用户的输入结合到一起。
     */
    val systemPrompt: String,
    val exampleType: ExampleType = ExampleType.NONE,
    val examples: List<CoTExample> = listOf(),
) {
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

    enum class Phase(val value: String) {
        // 归类
        Classify("classify"),

        // 澄清
        Clarify("clarify"),

        // 分析
        Analyze("analyze"),

        // 设计
        Design("design"),

        // 执行
        Execute("execute"),

        Custom("custom")
    }
}

