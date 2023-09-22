package cc.unitmesh.rag.store

import cc.unitmesh.cf.core.utils.IdUtil
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.similarity.JaccardSimilarity
import cc.unitmesh.nlp.similarity.RelevanceScore
import java.util.*

class InMemoryTextStore<Embedded> : EmbeddingStore<Embedded> {
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
            val cosineSimilarity = JaccardSimilarity.between(entry.embedding, referenceEmbedding)
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
}