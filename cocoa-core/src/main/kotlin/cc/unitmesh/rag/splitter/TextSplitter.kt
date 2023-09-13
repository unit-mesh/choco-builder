package cc.unitmesh.rag.splitter

import cc.unitmesh.rag.document.Document

const val DEFAULT_CHUNK_SIZE: Int = 4000
const val SENTENCE_CHUNK_OVERLAP: Int = 200

abstract class TextSplitter() : Splitter {
    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(TextSplitter::class.java)
    }

    override fun apply(documents: List<Document>): List<Document> {
        val texts: MutableList<String> = ArrayList()

        for (doc in documents) {
            texts.add(doc.text)
        }

        return createDocuments(texts)
    }

    private fun createDocuments(texts: List<String>): List<Document> {
        val documents: MutableList<Document> = mutableListOf()
        for (i in texts.indices) {
            val text = texts[i]
            val chunks: List<String> = splitText(text)
            if (chunks.size > 1) {
                log.info("Broke up document " + i + " into " + chunks.size + " chunks.")
            }
            for (chunk in chunks) {
                val newDoc: Document = Document(text = chunk)
                documents.add(newDoc)
            }
        }

        return documents
    }

    protected abstract fun splitText(text: String): List<String>
}