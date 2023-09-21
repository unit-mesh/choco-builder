package cc.unitmesh.rag

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.rag.base.ApplyDsl
import io.github.cdimascio.dotenv.Dotenv


/**
 * Apply is a DSL for invoking a function in a template.
 */
@ApplyDsl
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
     * Provider for LLM, like OpenAI, Azure OpenAI, etc.
     */
    var llm: LlmConnector = LlmConnector(LlmType.OpenAI, "", "")

    /**
     * Provider for vector store, like Elasticsearch, Memory, etc.
     */
    var store: Store = Store(StoreType.Elasticsearch)

    /**
     * Provider for embedding, like SentenceTransformers, etc.
     */
    var embedding: EmbeddingEngine = EmbeddingEngine(EngineType.SentenceTransformers)
        set(value) {
            field = value
            store.updateEmbedding(value.provider)
        }

    private var dsl: Dsl? = null;


    fun document(file: String): DocumentDsl {
        return DocumentDsl(file)
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

fun rag(name: String, init: Workflow.() -> Unit): Workflow {
    val workflow = Workflow(name)
    workflow.init()
    return workflow
}

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
