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
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class TextDocumentParser @JvmOverloads constructor(
    val documentType: DocumentType?,
    val charset: Charset = StandardCharsets.UTF_8,
) : DocumentParser {

    override fun parse(inputStream: InputStream): Document {
        return try {
            val buffer = ByteArrayOutputStream()
            var nRead: Int
            val data = ByteArray(1024)
            while (inputStream.read(data, 0, data.size).also { nRead = it } != -1) {
                buffer.write(data, 0, nRead)
            }
            buffer.flush()
            val text = String(buffer.toByteArray(), charset)
            Document.from(text, hashMapOf("documentType" to documentType.toString()))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
