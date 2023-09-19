package cc.unitmesh.rag.document

import java.io.InputStream

interface DocumentParser {
    /**
     * Parse the given input stream and return a list of documents.
     *
     * Multiple Documents' case:
     * - [cc.unitmesh.rag.document.DocumentType.PPT]
     *
     * One Document's case:
     * - [cc.unitmesh.rag.document.DocumentType.PDF]
     * - [cc.unitmesh.rag.document.DocumentType.TXT]
     * - [cc.unitmesh.rag.document.DocumentType.HTML]
     * - [cc.unitmesh.rag.document.DocumentType.DOC]
     */
    fun parse(inputStream: InputStream): List<Document>

    companion object {
        const val DOCUMENT_TYPE = "document_type"
    }
}
