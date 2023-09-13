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

import java.lang.String.join
import java.util.regex.Matcher
import java.util.regex.Pattern


class CharacterTextSplitter : TextSplitter() {
    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(CharacterTextSplitter::class.java)
    }

    protected var separator = "\n\n"

    /**
     * Split incoming text and return chunks.
     */
    override fun splitText(text: String): List<String> {
        // First, we naively split the large input into a bunch of smaller ones.
        val splits: List<String> = splitTextWithRegex(text, separator, keepSeparator)
        val mergedSeparator = if (keepSeparator) "" else separator
        return mergeSplits(splits, mergedSeparator)
    }

    /**
     * We now want to combine these smaller pieces into medium size chunks to send to the LLM.
     */
    fun mergeSplits(splits: List<String>, separator: String): List<String> {
        val separatorLength: Int = separator.length
        val docs: MutableList<String> = ArrayList()
        val currentDoc: MutableList<String> = ArrayList()
        var total = 0

        for (d in splits) {
            val length: Int = d.length
            if (total + length + (if (currentDoc.isNotEmpty()) separatorLength else 0) > chunkSize) {
                if (total > chunkSize) {
                    log.warn("Created a chunk of size {}, which is longer than the specified {}", total, chunkSize)
                }
                if (currentDoc.isNotEmpty()) {
                    val doc: String? = join(separator, currentDoc)
                    if (doc != null) {
                        docs.add(doc)
                    }
                    // we have a larger chunk than in the chunk overlap or if we still have any chunks and the length is
                    // long
                    while (total > chunkOverlap
                        || (total + length + (if (currentDoc.isNotEmpty()) separatorLength else 0) > chunkSize && total > 0)
                    ) {
                        total -= (currentDoc[0].length
                                + if (currentDoc.size > 1) separatorLength else 0)
                        currentDoc.removeAt(0)
                    }
                }
            }
            currentDoc.add(d)
            total += length + if (currentDoc.size > 1) separatorLength else 0
        }

        val doc: String? = join(separator, currentDoc)
        if (doc != null) {
            docs.add(doc)
        }

        return docs
    }

}

fun splitTextWithRegex(text: String, separator: String, keepSeparator: Boolean): List<String> {
    var splits: MutableList<String> = mutableListOf()
    if (separator.isNotEmpty()) {
        if (keepSeparator) {
            // The parentheses in the pattern keep the delimiters in the result.
            val parts: Array<String> = splitWithSeparator(text, separator)
            var i = 1
            while (i < parts.size - 1) {
                splits.add(parts[i] + parts[i + 1])
                i += 2
            }
            if (parts.size % 2 == 0) {
                splits.add(parts[parts.size - 1])
            }
            splits.add(0, parts[0])
        } else {
            splits = mutableListOf(*text.split(separator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        }
    } else {
        splits = mutableListOf(text)
    }

    return splits.filter { it.isNotEmpty() }
}

fun splitWithSeparator(text: String, separator: String?): Array<String> {
    val splits: MutableList<String> = ArrayList()
    val pattern: Pattern = Pattern.compile(("(" + Pattern.quote(separator)) + ")")
    val matcher: Matcher = pattern.matcher(text)
    var prevEnd = 0
    while (matcher.find()) {
        val start: Int = matcher.start()
        val end: Int = matcher.end()
        splits.add(text.substring(prevEnd, start))
        splits.add(text.substring(start, end))
        prevEnd = end
    }
    if (prevEnd < text.length) {
        splits.add(text.substring(prevEnd))
    }
    return splits.toTypedArray<String>()
}