package cc.unitmesh.document.parser;

import cc.unitmesh.docs.SampleCode
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

class MdDocumentParserTest {
    @Test
    @SampleCode
    fun should_parse_input_stream_to_list_of_documents() {
        // start-sample
        val parser = MdDocumentParser()
        val inputStream = ByteArrayInputStream("Sample Markdown Text".toByteArray())
        val result = parser.parse(inputStream)
        // end-sample

        // then
        result.size shouldBe 1
    }
}