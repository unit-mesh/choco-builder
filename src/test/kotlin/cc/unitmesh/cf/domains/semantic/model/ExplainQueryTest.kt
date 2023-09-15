package cc.unitmesh.cf.domains.semantic.model;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExplainQueryTest {

    @Test
    fun should_return_content_as_json_string() {
        // given
        val query = "SELECT * FROM table"
        val natureLangQuery = "Retrieve all records from the table"
        val hypotheticalDocument = "document"

        val explainQuery = ExplainQuery(query, natureLangQuery, hypotheticalDocument)

        // when
        val content = explainQuery.content

        // then
        assertEquals("""```explain-dsl
            |query: SELECT * FROM table
            |natureLangQuery: Retrieve all records from the table
            |hypotheticalDocument: document
            |```""".trimMargin(), content)
    }
}