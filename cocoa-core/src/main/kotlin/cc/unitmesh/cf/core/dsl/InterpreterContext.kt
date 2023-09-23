package cc.unitmesh.cf.core.dsl

import cc.unitmesh.nlp.embedding.EmbeddingElement
import cc.unitmesh.cf.core.utils.IdUtil

abstract class InterpreterContext(
    override val id: String = IdUtil.uuid(),
    override val name: String,
    val description: String,
) : EmbeddingElement {
}