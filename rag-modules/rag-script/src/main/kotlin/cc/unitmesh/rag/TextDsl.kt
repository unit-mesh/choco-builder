package cc.unitmesh.rag

import cc.unitmesh.document.parser.TextDocumentParser
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.document.DocumentParser
import cc.unitmesh.rag.document.DocumentType

class TextDsl(private val text: String) {
    private val documentParser: DocumentParser = TextDocumentParser(DocumentType.TXT)

    fun split(): List<Document> {
        return documentParser.parse(text.byteInputStream())
    }
}
