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
     * Provider for LLM Connector, like OpenAI, Azure OpenAI, etc.
     */
    var llm: LlmConnector = LlmConnector(LlmType.OpenAI, "", "")

    /**
     * Provider for vector store, like Elasticsearch, Milvus, InMemory, etc.
     */
    var store: Store = Store(StoreType.MemoryEnglish)

    /**
     * Provider for text embedding, like OpenAI, SentenceTransformers, etc.
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
     */
    fun document(file: String): DocumentDsl {
        return DocumentDsl.byFile(file)
    }

    /**
     * Directory is a function for indexing data for the workflow.
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
     */
    fun text(text: String): TextDsl {
        return TextDsl(text)
    }

    /**
     * Prepare is a function for preparing data for the workflow. You don't need to call it as block.
     */
    fun prepare(function: () -> Unit) {
        function()
    }

    /**
     * Indexing is a function for indexing data for the workflow. You don't need to call it as block.
     */
    fun indexing(function: () -> Unit) {
        // make sure embedding is updated
        store.updateEmbedding(embedding.provider)
        function()
    }

    /**
     * Querying is a function for querying data for the workflow. You don't need to call it as block.
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

    fun prompt(init: PromptScript.() -> Unit): PromptScript {
        val prompt = PromptScript()
        prompt.init()
        return prompt
    }
}

/**
 * rag is a function block, it will return a Workflow object.
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

fun prompt(init: PromptScript.() -> Unit): PromptScript {
    val prompt = PromptScript()
    prompt.init()
    return prompt
}
