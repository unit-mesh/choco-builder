package cc.unitmesh.cf.domains.semantic.model

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import kotlinx.serialization.Serializable


@Serializable
data class ExplainQuery(
    val query: String,
    val natureLangQuery: String,
    val hypotheticalDocument: String,
) : Dsl {
    override var domain: String = "semantic-search"
    override var interpreters: List<DslInterpreter> = listOf()
    override val content: String get() = """```explain-dsl
        |query: $query
        |natureLangQuery: $natureLangQuery
        |hypotheticalDocument: $hypotheticalDocument
        |```""".trimMargin()

}