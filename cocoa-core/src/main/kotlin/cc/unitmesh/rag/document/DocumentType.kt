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

package cc.unitmesh.rag.document

import java.util.*


enum class DocumentType(vararg supportedExtensions: String) {
    TXT("txt"),
    PDF("pdf"),
    HTML("html", "htm", "xhtml"),
    DOC("doc", "docx"),
    XLS("xls", "xlsx"),
    PPT("ppt", "pptx");

    private val supportedExtensions: Iterable<String>

    init {
        this.supportedExtensions = listOf(*supportedExtensions)
    }

    companion object {
        fun of(fileName: String): DocumentType {
            for (documentType in entries) {
                for (supportedExtension in documentType.supportedExtensions) {
                    if (fileName.endsWith(supportedExtension)) {
                        return documentType
                    }
                }
            }

            throw UnsupportedDocumentTypeException(fileName)
        }
    }
}


class UnsupportedDocumentTypeException(filePath: String?) :
    RuntimeException(String.format("Document type of '%s' is not supported", filePath))
