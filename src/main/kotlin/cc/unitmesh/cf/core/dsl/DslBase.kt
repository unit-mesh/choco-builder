package cc.unitmesh.cf.core.dsl

import cc.unitmesh.cf.infrastructure.utils.nextId
import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinx.serialization.Serializable

@Serializable
class DslBase(
    override val id: String = nextId(),
    val name: String,
    val description: String,
): BaseEmbedding {
    @JsonIgnore
    override lateinit var embedding: List<Double>
}
