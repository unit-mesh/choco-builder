package cc.unitmesh.rag

import cc.unitmesh.rag.base.ApplyDsl
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.nlp.embedding.EmbeddingProvider
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
    var env = try {
        Dotenv.load()
    } catch (e: Exception) {
        null
    }

    var llmConnector: LlmConnector = LlmConnector(LlmType.OpenAI, "", "")
    var retriever: Retriever = Retriever(StoreType.Elasticsearch)
    var embedding: EmbeddingEngine = EmbeddingEngine(EngineType.SentenceTransformers)
        get() = field
        set(value) {
            field = value
            retriever.updateEmbedding(value.provider)
        }

    var dsl: Dsl? = null;

    fun llm(prompt: String): Llm {
        return Llm(prompt)
    }

    fun embedding(engineType: EngineType = EngineType.SentenceTransformers): EmbeddingEngine {
        return EmbeddingEngine(engineType)
    }

    fun vectorStore(storeType: StoreType = StoreType.Elasticsearch): Retriever {
        return Retriever(storeType)
    }

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

class Retriever(storeType: StoreType) {
    private var embedding: EmbeddingProvider? = SentenceTransformersEmbedding()

    val store: EmbeddingStore<Document> = when (storeType) {
        StoreType.Elasticsearch -> ElasticsearchStore()
        StoreType.Memory -> InMemoryEmbeddingStore()
    }

    fun findRelevant(input: String): List<EmbeddingMatch<Document>> {
        val embedded = embedding!!.embed(input)
        return store.findRelevant(embedded, 20)
    }

    fun add(chunks: List<Document>): Boolean {
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

fun scripting(name: String, init: Workflow.() -> Unit): Workflow {
    val workflow = Workflow(name)
    workflow.init()
    return workflow
}
fun scripting(init: Workflow.() -> Unit): Workflow {
    val workflow = Workflow("scripting")
    workflow.init()
    return workflow
}
