package cc.unitmesh.rag.loader

import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.splitter.TextSplitter

interface Loader {
    fun load(): List<Document>

    fun load(textSplitter: TextSplitter): List<Document>
}