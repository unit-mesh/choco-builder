package cc.unitmesh.cf.core.dsl

import cc.unitmesh.cf.infrastructure.utils.uuid
import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinx.serialization.Serializable

@Serializable
class DslBase(
    override val id: String = uuid(),
    override val name: String,
    val description: String,
): EmbeddingElement {
    @JsonIgnore
    override lateinit var embedding: List<Double>
}
