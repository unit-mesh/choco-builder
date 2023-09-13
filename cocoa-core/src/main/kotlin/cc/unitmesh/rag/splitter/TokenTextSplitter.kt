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
package cc.unitmesh.rag.splitter

import com.knuddels.jtokkit.Encodings.*
import com.knuddels.jtokkit.api.Encoding
import com.knuddels.jtokkit.api.EncodingRegistry
import com.knuddels.jtokkit.api.EncodingType
import kotlin.math.max
import kotlin.math.min


class TokenTextSplitter(
    private var keepSeparator: Boolean = true
) : TextSplitter() {
    private val defaultChunkSize = 800 // The target size of each text

    // chunk in tokens
    private val minChunkSizeChars = 350 // The minimum size of each text

    // chunk in characters
    private val minChunkLengthToEmbed = 5 // Discard chunks shorter than this
    private val maxNumChunks = 10000 // The maximum number of chunks to generate from a


    private val registry: EncodingRegistry = newLazyEncodingRegistry()
    private val encoding: Encoding = registry.getEncoding(EncodingType.CL100K_BASE)
    override fun splitText(text: String): List<String> {
        return split(text, defaultChunkSize)
    }

    private fun split(text: String?, chunkSize: Int): List<String> {
        if (text == null || text.trim { it <= ' ' }.isEmpty()) {
            return ArrayList()
        }
        var tokens = getEncodedTokens(text)
        val chunks: MutableList<String> = ArrayList()
        var num_chunks = 0
        while (!tokens.isEmpty() && num_chunks < maxNumChunks) {
            val chunk = tokens.subList(
                0,
                min(chunkSize.toDouble(), tokens.size.toDouble()).toInt()
            )
            var chunkText = decodeTokens(chunk)

            // Skip the chunk if it is empty or whitespace
            if (chunkText.trim { it <= ' ' }.isEmpty()) {
                tokens = tokens.subList(chunk.size, tokens.size)
                continue
            }

            // Find the last period or punctuation mark in the chunk
            val lastPunctuation =
                max(
                    chunkText.lastIndexOf('.').toDouble(),
                    max(
                        chunkText.lastIndexOf('?').toDouble(),
                        max(chunkText.lastIndexOf('!').toDouble(), chunkText.lastIndexOf('\n').toDouble())
                    )
                )
                    .toInt()
            if (lastPunctuation != -1 && lastPunctuation > minChunkSizeChars) {
                // Truncate the chunk text at the punctuation mark
                chunkText = chunkText.substring(0, lastPunctuation + 1)
            }
            val chunk_text_to_append =
                if (keepSeparator) chunkText.trim { it <= ' ' } else chunkText.replace("\n", " ").trim { it <= ' ' }
            if (chunk_text_to_append.length > minChunkLengthToEmbed) {
                chunks.add(chunk_text_to_append)
            }

            // Remove the tokens corresponding to the chunk text from the remaining tokens
            tokens = tokens.subList(getEncodedTokens(chunkText).size, tokens.size)
            num_chunks++
        }

        // Handle the remaining tokens
        if (!tokens.isEmpty()) {
            val remaining_text = decodeTokens(tokens).replace("\n", " ").trim { it <= ' ' }
            if (remaining_text.length > minChunkLengthToEmbed) {
                chunks.add(remaining_text)
            }
        }
        return chunks
    }

    private fun getEncodedTokens(text: String): List<Int?> {
        return encoding.encode(text)
    }

    private fun decodeTokens(tokens: List<Int?>): String {
        return encoding.decode(tokens)
    }
}
