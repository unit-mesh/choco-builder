package cc.unitmesh.cf.code

import cc.unitmesh.rag.document.Document
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.jetbrains.annotations.TestOnly

/**
 * The `CodeSplitter` class is a solution for splitting code into smaller chunks for better semantic understanding and processing.
 * It is designed to handle large code files and split them into manageable portions.
 *
 * The class provides methods to split code based on specified parameters such as chunk size and maximum characters.
 * It also allows for adding a comment string to each chunk for better semantic understanding.
 *
 * The `CodeSplitter` class is part of the SweepAI code splitting solution and can be used to chunk code files.
 * For more information on code splitting, refer to the [SweepAI code splitting documentation](https://docs.sweep.dev/blogs/chunking-2m-files).
 *
 * This class can be found in the [llama_index](https://github.com/jerryjliu/llama_index) repository, specifically in the [pull request #7100](https://github.com/jerryjliu/llama_index/pull/7100).
 *
 * @param comment The comment string to prepend to each chunk for better semantic understanding. Defaults to "//".
 * @param chunkLines The number of lines per chunk. Defaults to 40 lines.
 * @param maxChars The maximum number of characters per chunk. Defaults to 1500 characters.
 * @param chunkLinesOverlap The number of overlapping lines between chunks. Defaults to 15 lines.
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
        var canonicalName = "$comment canonicalName: " + ds.Package + "." + ds.NodeName
        if (ds.Content.length <= maxChars) {
            return listOf(Document(canonicalName + "\n" + ds.Content))
        }

        return ds.Functions.map {
            // Kotlin primary constructor is not a function
            if (it.Name == "PrimaryConstructor") {
                return@map null
            }
            canonicalName += "." + it.Name
            split(it, canonicalName)
        }.filterNotNull()
    }

    @TestOnly
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