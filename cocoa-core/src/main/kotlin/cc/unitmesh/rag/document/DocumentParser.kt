package cc.unitmesh.rag.document

import java.io.InputStream

interface DocumentParser {
    fun parse(inputStream: InputStream): Document

    companion object {
        const val DOCUMENT_TYPE = "document_type"
    }
}
