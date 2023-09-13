package cc.unitmesh.rag.store

import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.nlp.embedding.EmbeddingProvider
import cc.unitmesh.nlp.similarity.CosineSimilarity
import cc.unitmesh.nlp.similarity.Similarity
import cc.unitmesh.nlp.similarity.SimilarityScore
import cc.unitmesh.rag.document.Document
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class InMemoryVectorStore(
    private val embeddingClient: EmbeddingProvider,
    override val similarity: Similarity = CosineSimilarity(),
) : VectorStore {
    private val store: ConcurrentHashMap<String, Document> = ConcurrentHashMap<String, Document>()

    override fun add(documents: List<Document>) {
        for (document in documents) {
            val embedding: Embedding = this.embeddingClient.embed(document)
            document.embedding = embedding
            this.store[document.id] = document
        }
    }

    override fun delete(idList: List<String>): Optional<Boolean> {
        for (id in idList) {
            store.remove(id)
        }
        return Optional.of(true)
    }

    override fun similaritySearch(query: String): List<Document> {
        return similaritySearch(query, 4)
    }

    override fun similaritySearch(query: String, k: Int): List<Document> {
        val userQueryEmbedding: List<Double> = queryEmbedding(query)
        return store.values
            .stream()
            .map { entry: Document ->
                val similarityScore = similarity.similarityScore(userQueryEmbedding, entry.embedding)
                SimilarityScore(entry.id, similarityScore)
            }
            .sorted(Comparator.comparingDouble(SimilarityScore::similarity).reversed())
            .limit(k.toLong())
            .map<Document> { s -> store[s.key] }
            .toList()
    }

    override fun similaritySearch(query: String, k: Int, threshold: Double): List<Document> {
        val userQueryEmbedding: List<Double> = queryEmbedding(query)
        return store.values
            .stream()
            .map { entry: Document ->
                val similarityScore = similarity.similarityScore(userQueryEmbedding, entry.embedding)
                SimilarityScore(entry.id, similarityScore)
            }
            .filter { s: SimilarityScore -> s.similarity >= threshold }
            .sorted(Comparator.comparingDouble(SimilarityScore::similarity).reversed())
            .limit(k.toLong())
            .map<Document> { s -> store[s.key] }
            .toList()
    }

    private fun queryEmbedding(query: String): List<Double> {
        return embeddingClient.embed(query)
    }
}