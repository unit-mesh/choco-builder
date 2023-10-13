package cc.unitmesh.document.parser

import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.document.DocumentParser
import cc.unitmesh.rag.splitter.MarkdownHeaderTextSplitter
import java.io.InputStream

/**
 * Markdown Document Parser
 */
class MdDocumentParser : DocumentParser {
    private val splitter = MarkdownHeaderTextSplitter(listOf())
    override fun parse(inputStream: InputStream): List<Document> {
        return splitter.splitText(inputStream.bufferedReader().readText())
    }
}