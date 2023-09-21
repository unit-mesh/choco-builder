package cc.unitmesh.rag

import cc.unitmesh.document.parser.MsOfficeDocumentParser
import cc.unitmesh.document.parser.PdfDocumentParser
import cc.unitmesh.document.parser.TextDocumentParser
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.document.DocumentParser
import cc.unitmesh.rag.document.DocumentType
import cc.unitmesh.rag.store.EmbeddingMatch
import java.io.File

class DocumentDsl(val file: String) {
    private val documentParser: DocumentParser

    init {
        val extension = file.substringAfterLast(".")
        val documentType = DocumentType.of(extension)
        documentParser = when (documentType) {
            DocumentType.TXT -> TextDocumentParser(documentType)
            DocumentType.PDF -> PdfDocumentParser()
            DocumentType.HTML -> TextDocumentParser(documentType)
            DocumentType.DOC -> MsOfficeDocumentParser(documentType)
            DocumentType.XLS -> MsOfficeDocumentParser(documentType)
            DocumentType.PPT -> MsOfficeDocumentParser(documentType)
        }
    }

    private val inputStream = File(file).inputStream()
    fun split(): List<Document> {
        return documentParser.parse(inputStream)
    }
}


// TODO: add order by score value
fun <T> Iterable<EmbeddingMatch<T>>.lowInMiddle(): List<EmbeddingMatch<T>> {
    val reversedDocuments = this.reversed()
    val reorderedResult = mutableListOf<EmbeddingMatch<T>>()

    for ((index, value) in reversedDocuments.withIndex()) {
        if (index % 2 == 1) {
            reorderedResult.add(value)
        } else {
            reorderedResult.add(0, value)
        }
    }

    return reorderedResult
}