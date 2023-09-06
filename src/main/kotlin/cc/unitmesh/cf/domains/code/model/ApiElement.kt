package cc.unitmesh.cf.domains.code.model

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.cf.core.dsl.IndexElement
import kotlinx.serialization.Serializable

@Serializable
class ApiElement(
    override val id: String,
    override val name: String,

    ) : Dsl, IndexElement {
    override var domain: String = "spring"
    override val content: String = ""
    override var interpreters: List<DslInterpreter> = listOf()
}