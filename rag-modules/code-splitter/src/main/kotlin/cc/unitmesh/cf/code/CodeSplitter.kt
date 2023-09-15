package cc.unitmesh.cf.code

import cc.unitmesh.rag.splitter.TextSplitter

/**
 * SweepAI code splitting solution.
 * https://docs.sweep.dev/blogs/chunking-2m-files
 * Also see in: https://github.com/jerryjliu/llama_index/pull/7100
 */
class CodeSplitter(
    val language: String,
    val chunkLines: Int = 40,
    val chunkLinesOverlap: Int = 15,
    val maxChars: Int = 1500
): TextSplitter() {
    override fun splitText(text: String): List<String> {
        TODO("Not yet implemented")
    }

}