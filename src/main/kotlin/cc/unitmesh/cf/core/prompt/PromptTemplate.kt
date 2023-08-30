package cc.unitmesh.cf.core.prompt

import cc.unitmesh.cf.infrastructure.utils.uuid
import jakarta.persistence.Id

class PromptTemplate(
    @Id
    val id: String = uuid(),
    val phase: Phase,
    val template: String,
    val examples: List<PromptExample>
) {
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
