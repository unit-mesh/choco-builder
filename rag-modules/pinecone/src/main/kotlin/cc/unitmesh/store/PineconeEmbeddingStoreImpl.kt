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

import cc.unitmesh.cf.core.utils.IdUtil
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.toEmbedding
import cc.unitmesh.nlp.similarity.CosineSimilarity
import cc.unitmesh.nlp.similarity.RelevanceScore
import cc.unitmesh.rag.document.TextSegment
import cc.unitmesh.rag.store.EmbeddingMatch
import cc.unitmesh.rag.store.EmbeddingStore
import com.google.protobuf.Struct
import com.google.protobuf.Value
import io.pinecone.PineconeClient
import io.pinecone.PineconeClientConfig
import io.pinecone.PineconeConnection
import io.pinecone.PineconeConnectionConfig
import io.pinecone.proto.*
import java.util.stream.Collectors

class PineconeEmbeddingStoreImpl(
    apiKey: String,
    environment: String,
    projectId: String,
    index: String,
    private var nameSpace: String = DEFAULT_NAMESPACE,
) : EmbeddingStore<TextSegment> {
    companion object {
        // do not change, will break backward compatibility!
        private const val DEFAULT_NAMESPACE = "default"

        // do not change, will break backward compatibility!
        private const val METADATA_TEXT_SEGMENT = "text_segment"
    }

    private var connection: PineconeConnection

    init {
        val configuration = PineconeClientConfig()
            .withApiKey(apiKey)
            .withEnvironment(environment)
            .withProjectName(projectId)

        val pineconeClient = PineconeClient(configuration)
        val connectionConfig = PineconeConnectionConfig().withIndexName(index)

        connection = pineconeClient.connect(connectionConfig)
    }

    override fun add(embedding: Embedding): String {
        val id: String = IdUtil.uuid()
        add(id, embedding)
        return id
    }

    override fun add(id: String, embedding: Embedding) {
        addInternal(id, embedding, null)
    }

    override fun add(embedding: Embedding, embedded: TextSegment): String {
        val id: String = IdUtil.uuid()
        addInternal(id, embedding, embedded)
        return id
    }

    private fun addInternal(id: String, embedding: Embedding, textSegment: TextSegment?) {
        addAllInternal(listOf(id), listOf(embedding), if (textSegment == null) null else listOf(textSegment))
    }

    private fun addAllInternal(ids: List<String>, embeddings: List<Embedding>, textSegments: List<TextSegment>?) {
        val upsertRequestBuilder = UpsertRequest.newBuilder()
            .setNamespace(nameSpace)

        for (i in embeddings.indices) {
            val id = ids[i]
            val embedding = embeddings[i]
            val vectorBuilder = Vector.newBuilder().setId(id).addAllValues(embedding.vectorAsList())

            if (textSegments != null) {
                val value = Value.newBuilder().setStringValue(textSegments[i].text).build()
                vectorBuilder.setMetadata(Struct.newBuilder().putFields(METADATA_TEXT_SEGMENT, value))
            }

            upsertRequestBuilder.addVectors(vectorBuilder.build())
        }

        connection.blockingStub.upsert(upsertRequestBuilder.build())
    }

    override fun addAll(embeddings: List<Embedding>): List<String> {
        val ids = embeddings.stream()
            .map { _ -> IdUtil.uuid() }
            .collect(Collectors.toList())
        addAllInternal(ids, embeddings, null)
        return ids
    }

    override fun addAll(embeddings: List<Embedding>, embedded: List<TextSegment>): List<String> {
        val ids = embeddings.stream()
            .map { _ -> IdUtil.uuid() }
            .collect(Collectors.toList())
        addAllInternal(ids, embeddings, embedded)
        return ids
    }

    override fun findRelevant(
        referenceEmbedding: Embedding,
        maxResults: Int,
        minScore: Double,
    ): List<EmbeddingMatch<TextSegment>> {
        val queryVector = QueryVector
            .newBuilder()
            .addAllValues(referenceEmbedding.vectorAsList())
            .setTopK(maxResults)
            .setNamespace(nameSpace)
            .build()

        val queryRequest = QueryRequest
            .newBuilder()
            .addQueries(queryVector)
            .setTopK(maxResults)
            .build()

        val matchedVectorIds = connection.blockingStub
            .query(queryRequest)
            .resultsList[0]
            .matchesList
            .stream()
            .map { obj: ScoredVector -> obj.getId() }
            .collect(Collectors.toList())
        if (matchedVectorIds.isEmpty()) {
            return emptyList()
        }

        val matchedVectors: Collection<Vector> = connection.blockingStub.fetch(
            FetchRequest.newBuilder()
                .addAllIds(matchedVectorIds)
                .setNamespace(nameSpace)
                .build()
        )
            .vectorsMap
            .values

        return matchedVectors.stream()
            .map { vector: Vector ->
                toEmbeddingMatch(vector, referenceEmbedding)
            }
            .filter { match: EmbeddingMatch<TextSegment> -> match.score >= minScore }
            .collect(Collectors.toList())
    }

    private fun toEmbeddingMatch(vector: Vector, referenceEmbedding: Embedding): EmbeddingMatch<TextSegment> {
        val textSegmentValue = vector.metadata.fieldsMap[METADATA_TEXT_SEGMENT]
        val embedding: Embedding = toEmbedding(vector.valuesList)
        val cosineSimilarity: Double = CosineSimilarity().similarityScore(embedding, referenceEmbedding)

        return when (textSegmentValue) {
            null -> EmbeddingMatch(
                RelevanceScore.fromCosineSimilarity(cosineSimilarity),
                vector.getId(),
                embedding,
                null as TextSegment
            )

            else -> EmbeddingMatch(
                RelevanceScore.fromCosineSimilarity(cosineSimilarity),
                vector.getId(),
                embedding,
                TextSegment.from(textSegmentValue.getStringValue())
            )
        }

    }
}

private fun <E> List<E>.vectorAsList(): MutableIterable<Float> {
    return this.map { it as Float }.toMutableList()
}
