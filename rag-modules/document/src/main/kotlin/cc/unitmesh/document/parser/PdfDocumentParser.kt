/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.unitmesh.document.parser

import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.document.DocumentParser
import cc.unitmesh.rag.document.DocumentType
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.IOException
import java.io.InputStream

/**
 * PdfDocumentParser is a class that implements the DocumentParser interface and is used to parse PDF documents.
 *
 * This class provides a method to parse a given input stream containing a PDF document and returns a list of Document objects.
 * Each Document object represents a parsed document and contains the extracted content along with additional metadata.
 *
 * The parse method reads the input stream, loads the PDF document using the Loader class, and extracts the text content using the PDFTextStripper class.
 * The extracted content is then used to create a Document object with the document type set to PDF.
 *
 * If an IOException occurs during the parsing process, a RuntimeException is thrown.
 *
 */
class PdfDocumentParser : DocumentParser {
    override fun parse(inputStream: InputStream): List<Document> {
        val documents: MutableList<Document> = mutableListOf()
        try {
            val pdfDocument: PDDocument = Loader.loadPDF(inputStream.readAllBytes())
            val stripper = PDFTextStripper()
            val content: String = stripper.getText(pdfDocument)
            pdfDocument.close()
            documents += Document.from(content, hashMapOf("documentType" to DocumentType.PDF))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        return documents
    }
}
