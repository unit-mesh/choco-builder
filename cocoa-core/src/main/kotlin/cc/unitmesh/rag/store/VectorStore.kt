package cc.unitmesh.rag.store

import cc.unitmesh.nlp.similarity.Similarity
import cc.unitmesh.rag.document.Document
import java.util.*


interface VectorStore<Embedded> {
    val similarity: Similarity
    fun add(documents: List<Embedded>)
    fun delete(idList: List<String>): Optional<Boolean>
    fun findRelevant(query: String): List<Embedded>
    fun findRelevant(query: String, maxResults: Int): List<Embedded>
    fun findRelevant(query: String, maxResults: Int, minSimilarity: Double): List<Embedded>
}
