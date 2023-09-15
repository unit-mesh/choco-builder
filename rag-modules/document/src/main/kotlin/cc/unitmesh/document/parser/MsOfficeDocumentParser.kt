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
import java.io.IOException
import java.io.InputStream

/**
 * Extracts text from a Microsoft Office document.
 * This parser supports various file formats, including ppt, pptx, doc, docx, xls, and xlsx.
 * For detailed information on supported formats, please refer to the [official Apache POI website](https://poi.apache.org/).
 */
class MsOfficeDocumentParser(val documentType: DocumentType) : DocumentParser {
    override fun parse(inputStream: InputStream): Document {
        try {
            ExtractorFactory.createExtractor(inputStream).use { extractor ->
                val text = extractor.text
                return Document.from(text, hashMapOf("documentType" to documentType.toString()))
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}
