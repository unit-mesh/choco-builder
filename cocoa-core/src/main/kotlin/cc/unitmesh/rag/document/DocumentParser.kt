package cc.unitmesh.rag.document

import java.io.InputStream

/**
 * The DocumentParser interface is responsible for parsing different types of documents.
 *
 * This interface is mainly based on the implementation of [Langchain4j](https://github.com/langchain4j/langchain4j).
 *
 * The DocumentParser interface returns multiple types of documents:
 * - [cc.unitmesh.rag.document.DocumentType.PPT]
 *
 * The DocumentParser interface also returns single types of documents:
 * - [cc.unitmesh.rag.document.DocumentType.PDF]
 * - [cc.unitmesh.rag.document.DocumentType.DOC]
 * - [cc.unitmesh.rag.document.DocumentType.XLS]
 * - [cc.unitmesh.rag.document.DocumentType.MD]
 * - [cc.unitmesh.rag.document.DocumentType.HTML]
 * - [cc.unitmesh.rag.document.DocumentType.TXT]
 *
 * The DocumentParser interface provides a method to parse the input stream of a document.
 *
 * @property DOCUMENT_TYPE The constant value representing the document type.
 */
interface DocumentParser {
    fun parse(inputStream: InputStream): List<Document>

    companion object {
        const val DOCUMENT_TYPE = "document_type"
    }
}
