package cc.unitmesh.rag

import cc.unitmesh.document.DocumentFactory
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.document.DocumentParser
import cc.unitmesh.rag.store.EmbeddingMatch
import java.io.File

/**
 * Document DSL for indexing document.
 */
class DocumentDsl(val path: String, val isDir: Boolean) {
    fun split(): List<Document> {
        val file = File(path)
        if (file.isFile) {
            val parser = parserByExt(file.extension)
            return parser.parse(file.inputStream())
        }

        if (file.isDirectory) {
            return file.walk()
                .filter { it.isFile }
                .map {
                    val parser = parserByExt(it.extension)
                    parser.parse(it.inputStream())
                }
                .flatten()
                .toList()
        }

        return emptyList()
    }

    companion object {
        fun byFile(file: String): DocumentDsl {
            return DocumentDsl(file, true)
        }

        fun byDir(directory: String): DocumentDsl {
            return DocumentDsl(directory, isDir = true)
        }

        fun parserByExt(extension: String): DocumentParser {
            return DocumentFactory.parserByExt(extension) ?: throw IllegalArgumentException("Unsupported file type: $extension")
        }
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