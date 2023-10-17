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
import org.apache.poi.extractor.ExtractorFactory
import org.apache.poi.xslf.usermodel.XMLSlideShow
import org.apache.poi.xslf.usermodel.XSLFSlide
import org.apache.poi.xslf.usermodel.XSLFTextShape
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.InputStream


/**
 * The `MsOfficeDocumentParser` class is responsible for extracting text from Microsoft Office documents.
 * It supports various file formats, including ppt, pptx, doc, docx, xls, and xlsx.
 * This class implements the `DocumentParser` interface.
 *
 * For detailed information on the supported formats, please refer to the official Apache POI website: [https://poi.apache.org/](https://poi.apache.org/).
 *
 * @param documentType The type of the Microsoft Office document to be parsed.
 */
open class MsOfficeDocumentParser(private val documentType: DocumentType) : DocumentParser {
    override fun parse(inputStream: InputStream): List<Document> {
        val documents: MutableList<Document> = mutableListOf()
        try {
            when (documentType) {
                DocumentType.PPT -> parseSlides(inputStream, documents)
                else -> {
                    documents += ExtractorFactory.createExtractor(inputStream).use { extractor ->
                        val text = extractor.text
                        Document.from(text, hashMapOf("documentType" to documentType.toString()))
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return documents
    }

    private fun parseSlides(
        inputStream: InputStream,
        documents: MutableList<Document>,
    ) {
        val ppt = XMLSlideShow(inputStream)

        // 遍历每一张幻灯片
        for (slide in ppt.slides) {
            // 获取幻灯片的文本内容
            val slideText = slide.title ?: ""
            val content = getTexts(slide)
            val metadata = hashMapOf("documentType" to documentType.toString(), "title" to slideText)
            documents += Document.from(content, metadata)
        }
        ppt.close()
    }

    private fun getTexts(slide: XSLFSlide): String {
        return slide.shapes.filter {
            it is XSLFTextShape
//                    && it.placeholder != Placeholder.TITLE
        }.joinToString("\n") {
            (it as XSLFTextShape).text
        }
    }
}
