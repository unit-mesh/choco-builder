package cc.unitmesh.cf.code

import cc.unitmesh.rag.document.Document
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction

/**
 * SweepAI code splitting solution.
 * https://docs.sweep.dev/blogs/chunking-2m-files
 * Also see in: https://github.com/jerryjliu/llama_index/pull/7100
 */
class CodeSplitter(
    private val comment: String = "//",
    private val chunkLines: Int = 40,
    val chunkLinesOverlap: Int = 15,
    private val maxChars: Int = 1500,
) {
    fun split(ds: CodeDataStruct): List<Document> {
        val canonicalName = "$comment canonicalName: " + ds.Package + "." + ds.NodeName
        return ds.Functions.map {
            split(it, canonicalName)
        }
    }

    fun split(it: CodeFunction, canonicalName: String): Document {
        var content = it.Content
        val lines = content.split("\n")
        if (lines.size > chunkLines) {
            content = lines.subList(0, chunkLines).joinToString("\n")
        }

        content = canonicalName + "\n" + content
        if (content.length > maxChars) {
            content = content.substring(0, maxChars)
        }

        return Document(content)
    }
}