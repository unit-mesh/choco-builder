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
    // the comment string to prepend to each chunk, for better semantic understanding
    private val comment: String = "//",
    // 1500 characters correspond approximately to 40 lines of code
    private val chunkLines: Int = 40,
    // the average token to chars ratio for code is ~1:5(300 tokens), and embedding models are capped at 512 tokens.
    private val maxChars: Int = 1500,
    // TODO: for unsupported languages, we can use the following heuristic to split the code
    val chunkLinesOverlap: Int = 15,
) {
    fun split(ds: CodeDataStruct): List<Document> {
        val canonicalName = "$comment canonicalName: " + ds.Package + "." + ds.NodeName
        return ds.Functions.map {
            if (ds.Content.length <= maxChars) {
                Document(ds.Content)
            } else {
                split(it, canonicalName)
            }
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