package cc.unitmesh.rag.retriever

import cc.unitmesh.rag.document.Document

interface Retriever {
    fun retrieve(query: String): List<Document>
}