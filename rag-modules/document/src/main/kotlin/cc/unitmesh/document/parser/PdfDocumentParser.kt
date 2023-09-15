package cc.unitmesh.document.parser

import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.document.DocumentParser
import cc.unitmesh.rag.document.DocumentType
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.IOException
import java.io.InputStream

class PdfDocumentParser : DocumentParser {
    override fun parse(inputStream: InputStream): Document {
        return try {
            val pdfDocument: PDDocument = Loader.loadPDF(inputStream.readAllBytes())
            val stripper = PDFTextStripper()
            val content: String = stripper.getText(pdfDocument)
            pdfDocument.close()
            Document.from(content, hashMapOf("documentType" to DocumentType.PDF))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}
