package cc.unitmesh.cf.core.dsl

import cc.unitmesh.nlp.embedding.EmbeddingElement
import cc.unitmesh.cf.core.utils.IdUtil
import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinx.serialization.Serializable

@Serializable
class DslInterpreter(
    override val id: String = IdUtil.uuid(),
    override val name: String,
    val description: String,
): EmbeddingElement {
    @JsonIgnore
    override lateinit var embedding: List<Double>
}
