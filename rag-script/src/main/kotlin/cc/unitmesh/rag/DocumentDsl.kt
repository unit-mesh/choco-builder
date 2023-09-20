package cc.unitmesh.rag

import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.store.EmbeddingMatch

class DocumentDsl(val file: String) {
    fun split() : List<Document> {
        return listOf()
    }

}


// TODO: add order by score value
fun <T> Iterable<EmbeddingMatch<T>>.lowInMiddle(): List<EmbeddingMatch<T>> {
    val reversedDocuments = this.reversed()
    val reorderedResult = mutableListOf<EmbeddingMatch<T>>()

    for ((index, value) in reversedDocuments.withIndex()) {
        if (index % 2 == 1) {
            reorderedResult.add(value)
        } else {
            reorderedResult.add(0, value)
        }
    }

    return reorderedResult
}