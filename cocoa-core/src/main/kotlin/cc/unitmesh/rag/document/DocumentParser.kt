package cc.unitmesh.rag.document

import java.io.InputStream

interface DocumentParser {
    /**
     * Parse the given input stream and return a list of documents.
     * multiple Documents' case:
     * [cc.unitmesh.rag.document.DocumentType.PPT], a single file may contain multiple documents
     * One Document's case:
     * [cc.unitmesh.rag.document.DocumentType.PDF], a single file may contain only one document
     * [cc.unitmesh.rag.document.DocumentType.TXT], a single file may contain only one document
     * [cc.unitmesh.rag.document.DocumentType.HTML], a single file may contain only one document
     * [cc.unitmesh.rag.document.DocumentType.DOC], a single file may contain only one document
     */
    fun parse(inputStream: InputStream): List<Document>

    companion object {
        const val DOCUMENT_TYPE = "document_type"
    }
}
