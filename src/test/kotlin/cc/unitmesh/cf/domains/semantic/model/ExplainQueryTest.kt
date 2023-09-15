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

        val explainQuery = ExplainQuery("get all records", query, natureLangQuery, hypotheticalDocument)

        // when
        val content = explainQuery.content

        // then
        assertEquals("""
            |question: get all records
            |answer:
            |query: SELECT * FROM table
            |natureLangQuery: Retrieve all records from the table
            |hypotheticalDocument: document
            |""".trimMargin(), content)
    }

    @Test
    fun should_parse_from_answer() {
        val query = ExplainQuery.parse("get all records", """
            |query: SELECT * FROM table
            |natureLangQuery: Retrieve all records from the table
            |hypotheticalDocument: 
            |```
            |document
            |```
            |""".trimMargin())

        assertEquals("get all records", query.question)
        assertEquals("SELECT * FROM table", query.query)
        assertEquals("Retrieve all records from the table", query.natureLangQuery)
        assertEquals("document", query.hypotheticalDocument)
    }
}