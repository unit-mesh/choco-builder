package cc.unitmesh.rag.document

import java.io.InputStream

/**
 * > 当前的 Chocolate Factory 主要基于 [Langchain4j](https://github.com/langchain4j/langchain4j) 的实现。
 *
 *  Parse the given input stream and return a list of documents.
 *
 *  返回多个 [Document]:
 *  - [cc.unitmesh.rag.document.DocumentType.PPT]
 *
 *  返回单个 [Document]:
 *  - [cc.unitmesh.rag.document.DocumentType.PDF]
 *  - [cc.unitmesh.rag.document.DocumentType.TXT]
 *  - [cc.unitmesh.rag.document.DocumentType.HTML]
 *  - [cc.unitmesh.rag.document.DocumentType.DOC]
 *
 */
interface DocumentParser {
    fun parse(inputStream: InputStream): List<Document>

    companion object {
        const val DOCUMENT_TYPE = "document_type"
    }
}
