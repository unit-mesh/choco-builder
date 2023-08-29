package cc.unitmesh.cf.factory.dsl

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*

fun nextId(): String {
    return UUID.randomUUID().toString()
}

class DslBase(
    override val id: String = nextId(),
    val name: String,
    val description: String,
): BaseEmbedding {
    @JsonIgnore
    override lateinit var embedding: List<Double>
}
