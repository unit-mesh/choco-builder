package cc.unitmesh.cf.core.dsl

import cc.unitmesh.cf.core.llms.EmbeddingElement
import cc.unitmesh.cf.core.utils.IdUtil
import jakarta.persistence.Id

abstract class InterpreterContext(
    @Id
    override val id: String = IdUtil.uuid(),

    override val name: String,
    val description: String,
) : EmbeddingElement {
}