package cc.unitmesh.cf.domains.semantic.model

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.cf.core.prompt.QAExample
import kotlinx.serialization.Serializable


@Serializable
data class ExplainQuery(
    val question: String,
    val query: String,
    val natureLangQuery: String,
    val hypotheticalDocument: String,
) : Dsl {
    override var domain: String = "semantic-search"
    override var interpreters: List<DslInterpreter> = listOf()
    override val content: String
        get() = """question: $question
        |answer:
        |```explain-dsl
        |query: $query
        |natureLangQuery: $natureLangQuery
        |hypotheticalDocument: $hypotheticalDocument
        |```""".trimMargin()

    companion object {
        val log = org.slf4j.LoggerFactory.getLogger(ExplainQuery::class.java)
        val EXAMPLES = listOf(
            ExplainQuery(
                question = "如何通过 ID 查找代码库变更信息?",
                query = "query git path change count by system id",
                natureLangQuery = "通过 ID 查找 Git 代码库路径变更统计信息",
                hypotheticalDocument = """
                    |```kotlin
                    |   @SqlQuery(
                    |       "select system_id as systemId, line_count as lineCount, path, changes" +
                    |       " from scm_path_change_count where system_id = :systemId"
                    |   )
                    |   fun findCountBySystemId(systemId: Long): List<GitPathChangeCount>
                    |```
                    |""".trimMargin()
            ),
            ExplainQuery(
                question = "What's the Qdrant threshold?",
                query = "Qdrant threshold (point, score, offset)",
                natureLangQuery = "Qdrant 阈值 (point, score, offset)",
                hypotheticalDocument = """
                    |```rust
                    |SearchPoints {{
                    |   limit,
                    |   vector: vectors.get(idx).unwrap().clone(),
                    |   collection_name: COLLECTION_NAME.to_string(),
                    |   offset: Some(offset),
                    |   score_threshold: Some(0.3),
                    |   with_payload: Some(WithPayloadSelector {{
                    |   selector_options: Some(with_payload_selector::SelectorOptions::Enable(true)),
                    |   }})
                    |```
                    |""".trimMargin()
            )
        )
        val QAExamples = EXAMPLES.map {
            QAExample(
                it.question, """
                |```explain-dsl
                |query: ${it.query}
                |natureLangQuery: ${it.natureLangQuery}
                |hypotheticalDocument: ${it.hypotheticalDocument}
                |```
                |""".trimMargin()
            )
        }
    }
}