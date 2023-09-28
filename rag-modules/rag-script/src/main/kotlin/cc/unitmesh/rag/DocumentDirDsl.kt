package cc.unitmesh.rag

import cc.unitmesh.rag.document.Document
import java.io.File

class DocumentDirDsl(val directory: String) {
    fun split(): List<Document> {
        return File(directory).walk()
            .filter { it.isFile }
            .map {
                val parser = DocumentDsl.parserByExt(it.extension)
                parser.parse(it.inputStream())
            }
            .flatten()
            .toList()
    }
}
