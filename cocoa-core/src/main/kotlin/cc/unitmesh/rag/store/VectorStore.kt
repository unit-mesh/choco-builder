package cc.unitmesh.rag.store

import cc.unitmesh.nlp.similarity.Similarity
import cc.unitmesh.rag.document.Document
import java.util.*


interface VectorStore {
    val similarity: Similarity
    fun add(documents: List<Document>)
    fun delete(idList: List<String>): Optional<Boolean>
    fun similaritySearch(query: String): List<Document>
    fun similaritySearch(query: String, k: Int): List<Document>
    fun similaritySearch(query: String, k: Int, threshold: Double): List<Document>
}
