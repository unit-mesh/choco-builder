package cc.unitmesh.rag.splitter

import cc.unitmesh.rag.document.Document

const val DEFAULT_CHUNK_SIZE: Int = 4000
const val SENTENCE_CHUNK_OVERLAP: Int = 200

abstract class BaseTextSplitter() : Splitter {
    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(BaseTextSplitter::class.java)
    }

    override fun apply(documents: List<Document>): List<Document> {
        val texts: MutableList<String> = ArrayList()
        val metadata: MutableMap<String, Any> = HashMap()

        for (doc in documents) {
            texts.add(doc.text)
            metadata.putAll(doc.metadata)
        }

        return createDocuments(texts, metadata)
    }

    private fun createDocuments(texts: List<String>, metadata: MutableMap<String, Any>): List<Document> {
        val documents: MutableList<Document> = mutableListOf()

        for (i in texts.indices) {
            val text = texts[i]
            for (chunk in splitText(text)) {
                val metadataCopy = metadata.entries.associate { (key, value) -> key to value }
                val newDoc = Document(chunk, metadata = metadataCopy)
                documents.add(newDoc)
            }
        }
        return documents
    }

    protected abstract fun splitText(text: String): List<String>
}