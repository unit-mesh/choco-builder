package cc.unitmesh.rag.store

import cc.unitmesh.cf.core.utils.IdUtil
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.similarity.CosineSimilarity
import cc.unitmesh.nlp.similarity.RelevanceScore
import cc.unitmesh.rag.document.Document
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.*

data class Entry<Embedded>(
    var id: String,
    var embedding: Embedding,
    var embedded: Embedded?,
)

class InMemoryEmbeddingStore<Embedded> : EmbeddingStore<Embedded> {
    private val entries: MutableList<Entry<Embedded>> = ArrayList()
    override fun add(embedding: Embedding): String {
        val id: String = IdUtil.uuid()
        add(id, embedding)
        return id
    }

    override fun add(id: String, embedding: Embedding) {
        add(id, embedding, null)
    }

    override fun add(embedding: Embedding, embedded: Embedded): String {
        val id: String = IdUtil.uuid()
        add(id, embedding, embedded)
        return id
    }

    private fun add(id: String, embedding: Embedding, embedded: Embedded?) {
        entries.add(Entry(id, embedding, embedded))
    }

    override fun addAll(embeddings: List<Embedding>): List<String> {
        val ids: MutableList<String> = ArrayList()
        for (embedding in embeddings) {
            ids.add(add(embedding))
        }
        return ids
    }

    override fun addAll(embeddings: List<Embedding>, embedded: List<Embedded>): List<String> {
        require(embeddings.size == embedded.size) { "The list of embeddings and embedded must have the same size" }
        val ids: MutableList<String> = ArrayList()
        for (i in embeddings.indices) {
            ids.add(add(embeddings[i], embedded[i]))
        }
        return ids
    }

    override fun findRelevant(
        referenceEmbedding: Embedding,
        maxResults: Int,
        minScore: Double,
    ): List<EmbeddingMatch<Embedded>> {
        val comparator = Comparator.comparingDouble(EmbeddingMatch<Embedded>::score)
        val matches = PriorityQueue(comparator)

        for (entry in entries) {
            val cosineSimilarity = CosineSimilarity.between(entry.embedding, referenceEmbedding)
            val score = RelevanceScore.fromCosineSimilarity(cosineSimilarity)
            if (score >= minScore) {
                matches.add(EmbeddingMatch(score, entry.id, entry.embedding, entry.embedded!!))

                if (matches.size > maxResults) {
                    matches.poll()
                }
            }
        }

        val result = ArrayList(matches)
        result.sortWith(comparator)
        result.reverse()
        return result
    }

    private fun serializeToJson(): String {
        return Gson().toJson(this)
    }

    private fun serializeToFile(filePath: Path?) {
        try {
            val json = serializeToJson()
            Files.write(filePath, json.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun serializeToFile(filePath: String) {
        serializeToFile(Paths.get(filePath))
    }

    private fun fromJson(json: String): InMemoryEmbeddingStore<Document> {
        val type: Type = object : TypeToken<InMemoryEmbeddingStore<Document>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun fromFile(filePath: Path?): InMemoryEmbeddingStore<Document> {
        return try {
            val json = String(Files.readAllBytes(filePath))
            fromJson(json)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun fromFile(filePath: String): InMemoryEmbeddingStore<Document> {
        return fromFile(Paths.get(filePath))
    }
}