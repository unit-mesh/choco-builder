package cc.unitmesh.rag.store

import cc.unitmesh.cf.core.utils.IdUtil
import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.similarity.JaccardSimilarity
import cc.unitmesh.nlp.similarity.RelevanceScore
import java.util.*

/**
 * A simple in-memory English implementation of [EmbeddingStore].
 *
 * This class represents an in-memory storage for English text embeddings. It implements the [EmbeddingStore] interface,
 * which provides methods for adding and retrieving embeddings.
 *
 * The class stores the embeddings in a mutable list of [Entry] objects. Each entry contains an ID, an embedding, and an
 * optional embedded object. The ID is generated using the [IdUtil.uuid] method. The class provides multiple overloaded
 * methods for adding embeddings, allowing the user to specify the ID and the embedded object.
 *
 * The class also provides methods for adding multiple embeddings at once. The [addAll] method takes a list of embeddings
 * and adds them to the store, returning a list of IDs for the added embeddings. There is also an overloaded version of
 * [addAll] that takes a list of embeddings and a list of embedded objects, ensuring that both lists have the same size.
 *
 * The [findRelevant] method allows the user to find the most relevant embeddings in the store based on a reference
 * embedding. It takes the reference embedding, the maximum number of results to return, and the minimum relevance score
 * as parameters. It calculates the cosine similarity between the reference embedding and each entry in the store, and
 * filters the entries based on the minimum score. The method returns a list of [EmbeddingMatch] objects, sorted by
 * relevance score in descending order.
 *
 * @param Embedded the type of the embedded object associated with each embedding
 */
class InMemoryEnglishTextStore<Embedded> : EmbeddingStore<Embedded> {
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