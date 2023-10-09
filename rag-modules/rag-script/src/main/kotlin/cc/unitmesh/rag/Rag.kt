package cc.unitmesh.rag

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.rag.base.RagScript
import io.github.cdimascio.dotenv.Dotenv


/**
 * RAGScript 是一个使用 Kotlin DSL 的脚本语言，以用于快速使用、构建 RAG （检索增强，Retrieval Augmented Generation）应用的 PoC。
 *
 * 适用场景：安装有 Intellij IDEA、Kotlin Jupyter 环境或者 Kotlin 编译器的开发环境。
 */
@RagScript
class Workflow(val name: String) {
    /**
     * env will load `.env` file in the root directory.
     */
    var env = try {
        Dotenv.load()
    } catch (e: Exception) {
        null
    }

    /**
     * Provider for LLM Connector, like OpenAI, etc.
     * for example:
     * ```kotlin
     * llm = LlmConnector(LlmType.OpenAI)
     * ```
     */
    var llm: LlmConnector = LlmConnector(LlmType.OpenAI, "", "")

    /**
     * Provider for vector store, like Elasticsearch, Milvus, InMemory, etc.
     * usage:
     * ```kotlin
     * store = Store(StoreType.Memory)
     * ```
     */
    var store: Store = Store(StoreType.MemoryEnglish)

    /**
     * Provider for text embedding, like OpenAI, SentenceTransformers, etc.
     * for example:
     * ```kotlin
     * embedding = EmbeddingEngine(EngineType.SentenceTransformers)
     * ```
     */
    var embedding: EmbeddingEngine = EmbeddingEngine(EngineType.EnglishTextEmbedding)
        set(value) {
            field = value
            store.updateEmbedding(value.provider)
        }

    private var dsl: Dsl? = null;

    /**
     * `document` function for provide document split for indexing, will auto-detect a file type.
     * support: txt, pdf, doc, docx, xls, xlsx, ppt, pptx
     * for example:
     * ```kotlin
     * // 从文件中读取文档
     * val document = document("filename.txt")
     * // 将文档切割成 chunk
     * val chunks = document.split()
     * ```
     */
    fun document(file: String): DocumentDsl {
        return DocumentDsl.byFile(file)
    }

    /**
     * Directory is a function for indexing data for the workflow.
     * for example:
     * ```kotlin
     * val docs = directory("docs")
     * val chunks = document.split()
     * ```
     */
    fun directory(directory: String): DocumentDsl {
        return DocumentDsl.byDir(directory)
    }

    /**
     * TODO: `code` function for provide code split for indexing.
     */
    fun code(file: String, language: String = "Kotlin"): CodeDsl {
        return CodeDsl(file)
    }

    /**
     * `text` function for provide text split for indexing.
     * for example:
     *
     * ```kotlin
     * val chunks = text("fun main(args: Array<String>) {\n    println(\"Hello, World!\")\n}").split()
     * ```
     */
    fun text(text: String): Text {
        return Text(text)
    }

    /**
     * Prepare is a function for preparing data for the workflow. You don't need to call it as block.
     */
    fun prepare(function: () -> Unit) {
        function()
    }

    /**
     * Indexing is a function block for indexing data for the workflow. You don't need to call it as block.
     * for example:
     * ```kotlin
     * indexing {
     *     // 从文件中读取文档
     *     val document = document("filename.txt")
     *     // 将文档切割成 chunk
     *     val chunks = document.split()
     *     // 建立索引
     *     store.indexing(chunks)
     * }
     * ```
     */
    fun indexing(function: () -> Unit) {
        // make sure embedding is updated
        store.updateEmbedding(embedding.provider)
        function()
    }

    /**
     * querying is a function block for querying data for the workflow. you don't need to call it as block.
     * for example:
     * ```kotlin
     * querying {
     *     // 查询
     *     store.findRelevant("workflow dsl design ").lowInMiddle().also {
     *         println(it)
     *     }
     * }
     * ```
     */
    fun querying(function: () -> Unit) {
        function()
    }

    /**
     * Problem space is a function for defining the problem.
     */
    fun problem(function: () -> Dsl) {
        dsl = function()
    }

    /**
     * Solution space is a function for defining the solution.
     */
    fun solution(function: (dsl: Dsl) -> Unit) {
        function(dsl!!)
    }

    /**
     * Step is for tagging function block only.
     */
    fun step(name: String, function: () -> Unit) {
        function()
    }

    fun prompt(init: PromptText.() -> Unit): PromptText {
        val prompt = PromptText()
        prompt.init()
        return prompt
    }
}

/**
 * rag is a function block, it will return a Workflow object.
 * for example:
 * ```kotlin
 *  rag {
 *    indexing {
 *        ...
 *    }
 *
 *    querying {
 *        ...
 *    }
 *}
 * ```
 */
fun rag(name: String, init: Workflow.() -> Unit): Workflow {
    val workflow = Workflow(name)
    workflow.init()
    return workflow
}

/**
 * rag is a function block, it will return a Workflow object.
 */
fun rag(init: Workflow.() -> Unit): Workflow {
    val workflow = Workflow("scripting")
    workflow.init()
    return workflow
}

fun prompt(init: PromptText.() -> Unit): PromptText {
    val prompt = PromptText()
    prompt.init()
    return prompt
}
