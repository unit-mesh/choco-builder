package cc.unitmesh.document.parser;

import cc.unitmesh.rag.document.DocumentType
import org.junit.jupiter.api.Test

class MsOfficeDocumentParserTest  {
    @Test
    fun should_parse_pptx_file() {
        val inputStream = this.javaClass.getResourceAsStream("/sample.pptx")
        val document = MsOfficeDocumentParser(DocumentType.PPT).parse(inputStream)

        assert(document.text.contains("代码库问答"))
    }
}