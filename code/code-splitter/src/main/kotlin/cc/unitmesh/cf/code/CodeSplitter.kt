package cc.unitmesh.cf.code

import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.splitter.Splitter
import chapi.domain.core.CodeDataStruct

/**
 * SweepAI code splitting solution.
 * https://docs.sweep.dev/blogs/chunking-2m-files
 * Also see in: https://github.com/jerryjliu/llama_index/pull/7100
 */
class CodeSplitter(
    val language: String,
    val chunkLines: Int = 40,
    val chunkLinesOverlap: Int = 15,
    val maxChars: Int = 1500,
) : Splitter {
    override fun apply(docs: List<Document>): List<Document> {
        return listOf()
    }

    fun split(ds: CodeDataStruct): List<Document> {
        return listOf()
    }
}