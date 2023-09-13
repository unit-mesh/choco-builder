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
package cc.unitmesh.store

import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.rag.document.TextSegment
import cc.unitmesh.rag.store.EmbeddingMatch
import cc.unitmesh.rag.store.EmbeddingStore

class MilvusEmbeddingStoreImpl : EmbeddingStore<TextSegment> {
    override fun add(embedding: Embedding): String {
        TODO("Not yet implemented")
    }

    override fun add(id: String, embedding: Embedding) {
        TODO("Not yet implemented")
    }

    override fun addAll(embeddings: List<Embedding>): List<String> {
        TODO("Not yet implemented")
    }

    override fun findRelevant(
        referenceEmbedding: Embedding,
        maxResults: Int,
        minScore: Double,
    ): List<EmbeddingMatch<TextSegment>> {
        TODO("Not yet implemented")
    }

    override fun addAll(embeddings: List<Embedding>, embedded: List<TextSegment>): List<String> {
        TODO("Not yet implemented")
    }

    override fun add(embedding: Embedding, embedded: TextSegment): String {
        TODO("Not yet implemented")
    }
}