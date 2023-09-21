package cc.unitmesh.rag

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.nlp.embedding.EmbeddingProvider
import cc.unitmesh.rag.base.ApplyDsl
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.store.EmbeddingMatch
import cc.unitmesh.rag.store.EmbeddingStore
import cc.unitmesh.rag.store.InMemoryEmbeddingStore
import cc.unitmesh.store.ElasticsearchStore
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
        get() = field
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
}

class Store(storeType: StoreType) {
    private var embedding: EmbeddingProvider? = SentenceTransformersEmbedding()

    private val store: EmbeddingStore<Document> = when (storeType) {
        StoreType.Elasticsearch -> ElasticsearchStore()
        StoreType.Memory -> InMemoryEmbeddingStore()
    }

    fun findRelevant(input: String): List<EmbeddingMatch<Document>> {
        val embedded = embedding!!.embed(input)
        return store.findRelevant(embedded, 20)
    }

    fun indexing(chunks: List<Document>): Boolean {
        val embeddings = chunks.map {
            embedding!!.embed(it.text)
        }

        store.addAll(embeddings, chunks)
        return true
    }

    fun updateEmbedding(value: EmbeddingProvider) {
        this.embedding = value
    }
}

enum class StoreType {
    Elasticsearch,
    Memory
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
