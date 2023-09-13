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
package cc.unitmesh.rag.splitter;

import cc.unitmesh.rag.document.Document
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


internal class MarkdownHeaderTextSplitterTest {
    /**
     * Test markdown splitter by header: Case 1.
     */
    @Test
    fun testMdHeaderTextSplitter1() {
        val markdownDocument = """
                # Foo

                    ## Bar

                Hi this is Jim

                Hi this is Joe

                 ## Baz

                 Hi this is Molly
                
                """.trimIndent()
        val headersToSplitOn: List<Pair<String, String>> = listOf(
            Pair("#", "Header 1"),
            Pair("##", "Header 2")
        )
        val markdownSplitter = MarkdownHeaderTextSplitter(headersToSplitOn)
        val output: List<Document> = markdownSplitter.splitText(markdownDocument)
        val expectedOutput: List<Document> = listOf(
            Document(
                "Hi this is Jim  \nHi this is Joe",
                mapOf("Header 1" to "Foo", "Header 2" to "Bar")
            ),
            Document(
                "Hi this is Molly",
                mapOf("Header 1" to "Foo", "Header 2" to "Baz")
            )
        )
        assertEquals(expectedOutput, output)
    }

    /**
     * Test markdown splitter by header: Case 2.
     */
    @Test
    fun testMdHeaderTextSplitter2() {
        val markdownDocument = """
                # Foo

                    ## Bar

                Hi this is Jim

                Hi this is Joe

                 ### Boo

                 Hi this is Lance

                 ## Baz

                 Hi this is Molly
                
                """.trimIndent()
        val headersToSplitOn: List<Pair<String, String>> = listOf(
            Pair("#", "Header 1"),
            Pair("##", "Header 2"),
            Pair("###", "Header 3")
        )
        val markdownSplitter = MarkdownHeaderTextSplitter(headersToSplitOn)
        val output: List<Document> = markdownSplitter.splitText(markdownDocument)
        val expectedOutput: List<Document> = listOf(
            Document(
                "Hi this is Jim  \nHi this is Joe",
                mapOf("Header 1" to "Foo", "Header 2" to "Bar")
            ),
            Document(
                "Hi this is Lance",
                mapOf("Header 1" to "Foo", "Header 2" to "Bar", "Header 3" to "Boo")
            ),
            Document(
                "Hi this is Molly",
                mapOf("Header 1" to "Foo", "Header 2" to "Baz")
            )
        )
        assertEquals(expectedOutput, output)
    }

    /**
     * Test markdown splitter by header: Case 3.
     */
    @Test
    fun testMdHeaderTextSplitter3() {
        val markdownDocument = """
                # Foo

                    ## Bar

                Hi this is Jim

                Hi this is Joe

                 ### Boo

                 Hi this is Lance

                 #### Bim

                 Hi this is John

                 ## Baz

                 Hi this is Molly
                
                """.trimIndent()
        val headersToSplitOn: List<Pair<String, String>> = listOf(
            Pair("#", "Header 1"),
            Pair("##", "Header 2"),
            Pair("###", "Header 3"),
            Pair("####", "Header 4")
        )
        val markdownSplitter = MarkdownHeaderTextSplitter(headersToSplitOn)
        val output: List<Document> = markdownSplitter.splitText(markdownDocument)
        val expectedOutput: List<Document> = listOf<Document>(
            Document(
                "Hi this is Jim  \nHi this is Joe",
                mapOf("Header 1" to "Foo", "Header 2" to "Bar")
            ),
            Document(
                "Hi this is Lance",
                mapOf("Header 1" to "Foo", "Header 2" to "Bar", "Header 3" to "Boo")
            ),
            Document(
                "Hi this is John",
                mapOf("Header 1" to "Foo", "Header 2" to "Bar", "Header 3" to "Boo", "Header 4" to "Bim")
            ),
            Document(
                "Hi this is Molly",
                mapOf("Header 1" to "Foo", "Header 2" to "Baz")
            )
        )
        assertEquals(expectedOutput, output)
    }
}