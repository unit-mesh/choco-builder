package cc.unitmesh.document.parser

import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.document.DocumentParser
import cc.unitmesh.rag.splitter.MarkdownHeaderTextSplitter
import java.io.InputStream

/**
 * Markdown Document Parser
 *
 * This class represents a parser for Markdown documents.
 *
 * The MdDocumentParser class uses the [MarkdownHeaderTextSplitter] class to split the text of the document
 * into separate sections based on the headers in the Markdown syntax. It then returns a list of Document
 * objects representing each section of the document.
 *
 * Example usage:
 * ```
 * val parser = MdDocumentParser()
 * val inputStream = FileInputStream("document.md")
 * val documents = parser.parse(inputStream)
 * ```
 *
 * @constructor Creates an instance of MdDocumentParser.
 */
class MdDocumentParser : DocumentParser {
    private val splitter = MarkdownHeaderTextSplitter(listOf())
    override fun parse(inputStream: InputStream): List<Document> {
        return splitter.splitText(inputStream.bufferedReader().readText())
    }
}