package cc.unitmesh.document

import cc.unitmesh.document.parser.MdDocumentParser
import cc.unitmesh.document.parser.MsOfficeDocumentParser
import cc.unitmesh.document.parser.PdfDocumentParser
import cc.unitmesh.document.parser.TextDocumentParser
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.document.DocumentParser
import cc.unitmesh.rag.document.DocumentType
import java.io.File
import java.nio.file.Path

class DocumentFactory {
    /**
     * Splits a given file into a list of documents based on its extension.
     *
     * @param fileName The name of the file to be split.
     * @return A list of documents parsed from the file.
     */
    fun split(fileName: String): List<Document> {
        val extension = fileName.substringAfterLast(".")
        val parser = parserByExt(extension) ?: return emptyList()
        return parser.parse(File(fileName).inputStream())
    }

    /**
     * Splits a directory into a list of documents.
     *
     * @param dir The directory to be split.
     * @return A list of documents extracted from the files in the directory.
     */
    fun split(dir: Path): List<Document> {
        return dir.toFile()
            .walk()
            .filter { it.isFile }
            .mapNotNull {
                val parser = parserByExt(it.extension) ?: return@mapNotNull null
                parser.parse(it.inputStream())
            }
            .flatten()
            .toList()
    }

    companion object {
        fun parserByExt(extension: String): DocumentParser? {
            return when (val documentType = DocumentType.of(extension)) {
                DocumentType.TXT -> TextDocumentParser(documentType)
                DocumentType.PDF -> PdfDocumentParser()
                DocumentType.HTML -> TextDocumentParser(documentType)
                DocumentType.DOC -> MsOfficeDocumentParser(documentType)
                DocumentType.XLS -> MsOfficeDocumentParser(documentType)
                DocumentType.PPT -> MsOfficeDocumentParser(documentType)
                DocumentType.MD -> MdDocumentParser()
                null -> null
            }
        }
    }
}