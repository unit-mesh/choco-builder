package cc.unitmesh.cf.core.prompt

import cc.unitmesh.cf.infrastructure.utils.nextId
import jakarta.persistence.Id
import kotlinx.serialization.Serializable

class PromptTemplate(
    @Id
    val id: String = nextId(),
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

@Serializable
data class PromptExample(
    val question: String,
    val answer: String,
)
