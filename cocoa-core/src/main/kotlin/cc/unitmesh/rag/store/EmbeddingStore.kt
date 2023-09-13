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
package cc.unitmesh.rag.store

import cc.unitmesh.nlp.embedding.Embedding


interface EmbeddingStore<Embedded> {
    fun add(embedding: Embedding): String

    fun add(id: String, embedding: Embedding)
    fun add(embedding: Embedding, embedded: Embedded): String
    fun addAll(embeddings: List<Embedding>): List<String>
    fun addAll(embeddings: List<Embedding>, embedded: List<Embedded>): List<String>

    fun findRelevant(referenceEmbedding: Embedding, maxResults: Int): List<EmbeddingMatch<Embedded>> {
        return findRelevant(referenceEmbedding, maxResults, 0.0)
    }

    fun findRelevant(referenceEmbedding: Embedding, maxResults: Int, minScore: Double): List<EmbeddingMatch<Embedded>>
}