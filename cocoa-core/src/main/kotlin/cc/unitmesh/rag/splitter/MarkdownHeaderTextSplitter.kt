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

import cc.unitmesh.rag.document.Document


data class HeaderType(val level: Int, val name: String, val data: String)
data class LineType(var content: String, val metadata: Map<String, Any>)

/**
 * The `MarkdownHeaderTextSplitter` class is responsible for splitting a markdown file into chunks based on specified headers.
 * It allows for the extraction of content associated with each header and provides options for aggregating the content into chunks
 * with common headers or returning each line separately.
 *
 * @property headersToSplitOn The list of headers to split on, along with their associated metadata.
 * @property returnEachLine A flag indicating whether to return each line separately or aggregate them into chunks with common headers.
 *
 * @constructor Creates a new instance of the `MarkdownHeaderTextSplitter` class with the specified headers to split on and aggregation option.
 * @param headersToSplitOn The list of headers to split on, along with their associated metadata.
 * @param returnEachLine A flag indicating whether to return each line separately or aggregate them into chunks with common headers.
 *
 * 在使用本地的 Sentence Transformers 的 all-MiniLM-L6-v2 模型，由于受输出是 384 的长度影响，需要控制好 token 的长度。 因此，需要对文档进行二次分割。
 *
 */
class MarkdownHeaderTextSplitter(
    headersToSplitOn: List<Pair<String, String?>>,
    private val returnEachLine: Boolean,
) {
    /**
     * Headers we want to track
     */
    private val headersToSplitOn: List<Pair<String, String?>>

    constructor(headersToSplitOn: List<Pair<String, String>>) : this(headersToSplitOn, false)

    init {
        // Output line-by-line or aggregated into chunks w/ common headers

        // Given the headers we want to split on, (e.g., "#, ##, etc") order by length
        this.headersToSplitOn = headersToSplitOn.stream()
            .sorted(Comparator.comparingInt { e: Pair<String, String?> ->
                e.first.length
            }.reversed())
            .toList()
    }

    /**
     * Combine lines with common metadata into chunks.
     *
     * @param lines Line of text / associated header metadata
     * @return List of Document chunks
     */
    private fun aggregateLinesToChunks(lines: List<LineType>): MutableList<Document> {
        val aggregatedChunks: MutableList<LineType> = ArrayList<LineType>()
        for (line in lines) {
            if (aggregatedChunks.isNotEmpty() && aggregatedChunks[aggregatedChunks.size - 1].metadata == line.metadata) {
                // If the last line in the aggregated list has the same metadata as the current line,
                // append the current content to the last line's content
                val lastChunk: LineType = aggregatedChunks[aggregatedChunks.size - 1]
                lastChunk.content = lastChunk.content + "  \n" + line.content
            } else {
                // Otherwise, append the current line to the aggregated list
                aggregatedChunks.add(line)
            }
        }
        return aggregatedChunks.stream()
            .map { chunk: LineType ->
                Document(
                    text = chunk.content,
                    metadata = chunk.metadata
                )
            }
            .toList()
    }

    /**
     * Split markdown file.
     *
     * @param text Markdown file
     * @return List of Document chunks
     */
    fun splitText(text: String): List<Document> {
        val linesWithMetadata: MutableList<LineType> = ArrayList()
        // Content and metadata of the chunk currently being processed
        val currentContent: MutableList<String> = ArrayList()
        var currentMetadata: Map<String, Any> = mapOf()
        // Keep track of the nested header structure
        val headerStack: MutableList<HeaderType> = ArrayList()
        val initialMetadata: MutableMap<String, String> = mutableMapOf()

        // Split the input text by newline character ("\n").
        val lines = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (line in lines) {
            val strippedLine = line.trim()
            // Check each line against each of the header types (e.g., #, ##)
            val foundHeader = processLine(
                strippedLine, linesWithMetadata, currentContent, currentMetadata,
                headerStack, initialMetadata
            )
            if (!foundHeader && strippedLine.isNotEmpty()) {
                currentContent.add(strippedLine)
            } else if (currentContent.isNotEmpty()) {
                linesWithMetadata.add(
                    LineType(
                        java.lang.String.join("\n", currentContent),
                        currentMetadata
                    )
                )
                currentContent.clear()
            }
            currentMetadata = HashMap<String, Any>(initialMetadata)
        }

        return processOutput(linesWithMetadata, currentContent, currentMetadata)
    }

    private fun processLine(
        strippedLine: String,
        linesWithMetadata: MutableList<LineType>,
        currentContent: MutableList<String>,
        currentMetadata: Map<String, Any>,
        headerStack: MutableList<HeaderType>,
        initialMetadata: MutableMap<String, String>,
    ): Boolean {
        for (pair in headersToSplitOn) {
            val sep: String = pair.first
            val name: String? = pair.second
            if (isHeaderToSplitOn(strippedLine, sep)) {
                // Ensure we are tracking the header as metadata
                if (name != null) {
                    // Get the current header level
                    val currentHeaderLevel: Int = sep.count { it == '#' }
                    // Pop out headers of lower or same level from the stack
                    while (headerStack.isNotEmpty()
                        && headerStack[headerStack.size - 1].level >= currentHeaderLevel
                    ) {
                        // We have encountered a new header at the same or higher level
                        val poppedHeader: HeaderType = headerStack.removeAt(headerStack.size - 1)
                        // Clear the metadata for the popped header in initialMetadata
                        initialMetadata.remove(poppedHeader.name)
                    }
                    // Push the current header to the stack
                    val header = HeaderType(currentHeaderLevel, name, strippedLine.substring(sep.length).trim())
                    headerStack.add(header)
                    // Update initialMetadata with the current header
                    initialMetadata[name] = header.data
                }
                // Add the previous line to the linesWithMetadata only if currentContent is not empty
                if (currentContent.isNotEmpty()) {
                    linesWithMetadata.add(LineType(currentContent.joinToString("\n"), currentMetadata))
                    currentContent.clear()
                }
                return true
            }
        }
        return false
    }

    /**
     * Check if line starts with a header that we intend to split on.
     * Header with no text OR header is followed by space Both are valid conditions that sep is being used a header.
     */
    private fun isHeaderToSplitOn(strippedLine: String, sep: String): Boolean {
        return strippedLine.startsWith(sep) &&
                (strippedLine.length == sep.length || strippedLine[sep.length] == ' ')
    }

    private fun processOutput(
        linesWithMetadata: MutableList<LineType>, currentContent: List<String>,
        currentMetadata: Map<String, Any>,
    ): List<Document> {
        if (currentContent.isNotEmpty()) {
            linesWithMetadata.add(LineType(currentContent.joinToString("\n"), currentMetadata))
        }
        // linesWithMetadata has each line with associated header metadata aggregate these into chunks based on common
        // metadata
        return if (!returnEachLine) {
            aggregateLinesToChunks(linesWithMetadata)
        } else {
            linesWithMetadata.stream()
                .map { chunk: LineType ->
                    Document(
                        text = chunk.content,
                        metadata = chunk.metadata
                    )
                }
                .toList()
        }
    }
}