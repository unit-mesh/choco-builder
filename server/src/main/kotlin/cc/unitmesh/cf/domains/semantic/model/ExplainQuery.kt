package cc.unitmesh.cf.domains.semantic.model

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.cf.core.parser.MarkdownCode
import cc.unitmesh.cf.core.prompt.QAExample
import kotlinx.serialization.Serializable
import org.slf4j.Logger


@Serializable
data class ExplainQuery(
    val question: String,
    val englishQuery: String,
    val originLanguageQuery: String,
    val hypotheticalCode: String,
) : Dsl {
    override var domain: String = "semantic-search"
    override var interpreters: List<DslInterpreter> = listOf()
    override val content: String
        get() = """question: $question
        |answer:
        |englishQuery: $englishQuery
        |originLanguageQuery: $originLanguageQuery
        |hypotheticalCode: $hypotheticalCode
        |""".trimMargin()

    companion object {
        fun parse(question: String, completion: String): ExplainQuery {
            val code = MarkdownCode.parse(completion)

            val query = completion.substringAfter("englishQuery: ").substringBefore("\n")
            val natureLangQuery = completion.substringAfter("originLanguageQuery: ").substringBefore("\n")
            var hypotheticalCode = code.text

            if (hypotheticalCode.isBlank()) {
                hypotheticalCode = completion.substringAfter("hypotheticalCode: ")
            }

            return ExplainQuery(question, query, natureLangQuery, hypotheticalCode)
        }

        val log: Logger = org.slf4j.LoggerFactory.getLogger(ExplainQuery::class.java)!!
        val EXAMPLES = listOf(
            ExplainQuery(
                question = "如何通过 ID 查找代码库变更信息?",
                englishQuery = "query git path change count by system id",
                originLanguageQuery = "// 通过 id 查找 Git 代码库路径变更统计信息",
                hypotheticalCode = """
                    |```kotlin
                    |   @SqlQuery(
                    |       "select system_id as systemId, line_count as lineCount, path, changes" +
                    |       " from scm_path_change_count where system_id = :systemId"
                    |   )
                    |   fun findCountBySystemId(systemId: Long): List<GitPathChangeCount>
                    |```""".trimMargin()
            ),
            ExplainQuery(
                question = "What's the Qdrant threshold?",
                englishQuery = "Qdrant threshold (point, score, offset)",
                originLanguageQuery = "// Threshold 结构体用于定义搜索点的筛选条件和参数",
                hypotheticalCode = """
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
                    |```""".trimMargin()
            )
        )
        val QAExamples = EXAMPLES.map {
            QAExample(
                it.question, """
                |englishQuery: ${it.englishQuery}
                |originLanguageQuery: ${it.originLanguageQuery}
                |hypotheticalCode: 
                |${it.hypotheticalCode}
                |###
                |""".trimMargin()
            )
        }
    }
}