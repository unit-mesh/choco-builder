package cc.unitmesh.store

import cc.unitmesh.cf.core.utils.IdUtil
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.store.EmbeddingMatch
import cc.unitmesh.rag.store.EmbeddingStore
import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._types.InlineScript
import co.elastic.clients.elasticsearch._types.Script
import co.elastic.clients.elasticsearch._types.mapping.DenseVectorProperty
import co.elastic.clients.elasticsearch._types.mapping.Property
import co.elastic.clients.elasticsearch._types.mapping.TextProperty
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery
import co.elastic.clients.elasticsearch._types.query_dsl.Query
import co.elastic.clients.elasticsearch._types.query_dsl.ScriptScoreQuery
import co.elastic.clients.elasticsearch.core.BulkRequest
import co.elastic.clients.elasticsearch.core.SearchRequest
import co.elastic.clients.elasticsearch.core.SearchResponse
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest
import co.elastic.clients.elasticsearch.indices.ExistsRequest
import co.elastic.clients.json.JsonData
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.ElasticsearchTransport
import co.elastic.clients.transport.rest_client.RestClientTransport
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.Header
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.apache.http.message.BasicHeader
import org.elasticsearch.client.RestClient
import java.io.IOException
import java.util.*
import java.util.stream.Collectors

/**
 * Elastic Embedding Store Implementation
 */
class ElasticsearchEmbeddingStoreImpl(
    serverUrl: String,
    username: String?,
    password: String?,
    apiKey: String?,
    indexName: String,
) : EmbeddingStore<Document> {
    private val client: ElasticsearchClient
    private val indexName: String?
    private val objectMapper: ObjectMapper

    init {
        val serverUrl = serverUrl
        val indexName = indexName

        val restClientBuilder = RestClient
            .builder(HttpHost.create(serverUrl))

        if (!username.isNullOrBlank()) {
            val provider: CredentialsProvider = BasicCredentialsProvider()
            provider.setCredentials(AuthScope.ANY, UsernamePasswordCredentials(username, password))
            restClientBuilder.setHttpClientConfigCallback { httpClientBuilder: HttpAsyncClientBuilder ->
                httpClientBuilder.setDefaultCredentialsProvider(
                    provider
                )
            }
        }
        if (!apiKey.isNullOrBlank()) {
            restClientBuilder.setDefaultHeaders(
                arrayOf<Header>(
                    BasicHeader("Authorization", "Apikey $apiKey")
                )
            )
        }
        val transport: ElasticsearchTransport = RestClientTransport(restClientBuilder.build(), JacksonJsonpMapper())
        client = ElasticsearchClient(transport)
        this.indexName = indexName
        objectMapper = ObjectMapper()
    }

    override fun add(embedding: Embedding): String {
        val id: String = IdUtil.uuid()
        add(id, embedding)
        return id
    }

    override fun add(id: String, embedding: Embedding) {
        addInternal(id, embedding, null)
    }

    override fun add(embedding: Embedding, document: Document): String {
        val id: String = IdUtil.uuid()
        addInternal(id, embedding, document)
        return id
    }

    override fun addAll(embeddings: List<Embedding>): List<String> {
        val ids = embeddings.stream()
            .map { _: Embedding -> IdUtil.uuid() }
            .collect(Collectors.toList())
        addAllInternal(ids, embeddings, null)
        return ids
    }

    override fun addAll(embeddings: List<Embedding>, embedded: List<Document>): List<String> {
        val ids = embeddings.stream()
            .map { _: Embedding -> IdUtil.uuid() }
            .collect(Collectors.toList())
        addAllInternal(ids, embeddings, embedded)
        return ids
    }

    override fun findRelevant(
        referenceEmbedding: Embedding,
        maxResults: Int,
        minScore: Double,
    ): List<EmbeddingMatch<Document>> {
        return try {
            // Use Script Score and cosineSimilarity to calculate
            // see https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-script-score-query.html#vector-functions-cosine
            val scriptScoreQuery = buildDefaultScriptScoreQuery(referenceEmbedding, minScore.toFloat())
            val response: SearchResponse<Document> = client.search(
                SearchRequest.of { s: SearchRequest.Builder ->
                    s.query { n: Query.Builder -> n.scriptScore(scriptScoreQuery) }
                        .size(maxResults)
                }, Document::class.java
            )
            toEmbeddingMatch(response)
        } catch (e: IOException) {
            log.error("[ElasticSearch encounter I/O Exception]", e)
            throw ElasticsearchRequestFailedException(e.message!!)
        }
    }

    private fun addInternal(id: String, embedding: Embedding, embedded: Document?) {
        addAllInternal(
            listOf<String>(id),
            listOf<Embedding>(embedding),
            if (embedded == null) null else listOf<Document>(embedded)
        )
    }

    private fun addAllInternal(ids: List<String>, embeddings: List<Embedding>, embedded: List<Document>?) {
        if (ids.isEmpty() || embeddings.isEmpty()) {
            log.info("[do not add empty embeddings to elasticsearch]")
            return
        }
        if (ids.size != embeddings.size) {
            throw IllegalArgumentException("ids size is not equal to embeddings size")
        }
        if (embedded != null && embeddings.size != embedded.size) {
            throw IllegalArgumentException("embeddings size is not equal to embedded size")
        }
        try {
            createIndexIfNotExist(embeddings[0].size)
            bulk(ids, embeddings, embedded)
        } catch (e: IOException) {
            log.error("[ElasticSearch encounter I/O Exception]", e)
            throw ElasticsearchRequestFailedException(e.message!!)
        }
    }

    @Throws(IOException::class)
    private fun createIndexIfNotExist(dim: Int) {
        val response = client.indices().exists { c: ExistsRequest.Builder -> c.index(indexName) }
        if (!response.value()) {
            client.indices().create { c: CreateIndexRequest.Builder ->
                c.index(indexName)
                    .mappings(getDefaultMappings(dim))
            }
        }
    }

    private fun getDefaultMappings(dim: Int): TypeMapping {
        // do this like LangChain do
        val properties: MutableMap<String, Property> = HashMap(4)
        properties["text"] =
            Property.of { p: Property.Builder -> p.text(TextProperty.of { t: TextProperty.Builder? -> t }) }
        properties["vector"] = Property.of { p: Property.Builder ->
            p.denseVector(DenseVectorProperty.of { d: DenseVectorProperty.Builder ->
                d.dims(dim)
            })
        }
        return TypeMapping.of { c: TypeMapping.Builder -> c.properties(properties) }
    }

    @Throws(IOException::class)
    private fun bulk(ids: List<String>, embeddings: List<Embedding>, embedded: List<Document>?) {
        val size = ids.size
        val bulkBuilder = BulkRequest.Builder()
        for (i in 0 until size) {
            val document: Document = Document(
                text = embedded?.get(i)?.text ?: "",
                metadata = embedded?.get(i)?.metadata ?: HashMap(),
                vector = embeddings[i]
            )

            bulkBuilder.operations { op: BulkOperation.Builder ->
                op.index { idx: IndexOperation.Builder<Any?> ->
                    idx
                        .index(indexName)
                        .id(ids[i])
                        .document(document)
                }
            }
        }
        val response = client.bulk(bulkBuilder.build())
        if (response.errors()) {
            for (item in response.items()) {
                if (item.error() != null) {
                    throw ElasticsearchRequestFailedException(
                        "type: " + item.error()!!
                            .type() + ", reason: " + item.error()!!.reason()
                    )
                }
            }
        }
    }

    @Throws(JsonProcessingException::class)
    private fun buildDefaultScriptScoreQuery(vector: Embedding, minScore: Float): ScriptScoreQuery {
        val queryVector = toJsonData(vector)
        return ScriptScoreQuery.of { q: ScriptScoreQuery.Builder ->
            q
                .minScore(minScore)
                .query(Query.of { qu: Query.Builder -> qu.matchAll { m: MatchAllQuery.Builder? -> m } })
                .script { s: Script.Builder ->
                    s.inline(InlineScript.of { i: InlineScript.Builder ->
                        i // The script adds 1.0 to the cosine similarity to prevent the score from being negative.
                            // divided by 2 to keep score in the range [0, 1]
                            .source("(cosineSimilarity(params.query_vector, 'vector') + 1.0) / 2")
                            .params("query_vector", queryVector)
                    })
                }
        }
    }

    @Throws(JsonProcessingException::class)
    private fun <T> toJsonData(rawData: T): JsonData {
        return JsonData.fromJson(objectMapper.writeValueAsString(rawData))
    }

    private fun toEmbeddingMatch(response: SearchResponse<Document>): List<EmbeddingMatch<Document>> {
        return response.hits().hits().map { hit ->
            val document = hit.source() ?: return@map null
            val segmentEmbeddingMatch = if (document.text == null && document.text.isNotEmpty()) {
                return@map null
            } else {
                EmbeddingMatch(hit.score()!!, hit.id(), document.vector, Document(document.text, document.metadata))
            }
            segmentEmbeddingMatch
        }.filterNotNull()
    }

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(ElasticsearchEmbeddingStoreImpl::class.java)
    }
}
