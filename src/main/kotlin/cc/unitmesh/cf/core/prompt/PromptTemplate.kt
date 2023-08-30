package cc.unitmesh.cf.core.prompt

import cc.unitmesh.cf.infrastructure.utils.Constants
import cc.unitmesh.cf.infrastructure.utils.nextId
import jakarta.persistence.Column
import jakarta.persistence.Id
import org.hibernate.annotations.Comment

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

data class PromptExample(
    val question: String,
    val answer: String,
)
