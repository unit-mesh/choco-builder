package cc.unitmesh.cf.core.prompt

import cc.unitmesh.cf.infrastructure.utils.uuid
import jakarta.persistence.Id

data class PromptTemplate(
    @Id
    val id: String = uuid(),
    val phase: Phase,
    val template: String,
    val exampleType: ExampleType = ExampleType.NONE,
    val examples: List<PromptExample> = listOf(),
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

    enum class Phase {
        // 归类
        Classify,

        // 澄清
        Clarify,

        // 分析
        Analyze,

        // 设计
        Design,

        // 执行
        Execute,
    }
}
