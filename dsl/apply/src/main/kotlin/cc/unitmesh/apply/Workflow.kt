package cc.unitmesh.apply

import cc.unitmesh.apply.base.ApplyDsl
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.store.EmbeddingMatch
import cc.unitmesh.store.ElasticsearchStore

/**
 * Apply is a DSL for invoking a function in a template.
 */
@ApplyDsl
class Workflow(val name: String) {
    var connection: Connection = Connection(ConnectionType.OpenAI)
    var embedding: EmbeddingEngine = EmbeddingEngine(EngineType.SentenceTransformers)
    var vectorStore: VectorStore = VectorStore(StoreType.Elasticsearch)

    var dsl: Dsl? = null;

    fun llm(prompt: String): Llm {
        return Llm(prompt)
    }

    fun embedding(engineType: EngineType = EngineType.SentenceTransformers): EmbeddingEngine {
        return EmbeddingEngine(engineType)
    }

    fun vectorStore(storeType: StoreType = StoreType.Elasticsearch): VectorStore {
        return VectorStore(storeType)
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

class VectorStore(storeType: StoreType) {
    val store: ElasticsearchStore = when (storeType) {
        StoreType.Elasticsearch -> ElasticsearchStore()
    }

    fun query(input: String): List<EmbeddingMatch<Document>> {
//        return store.findRelevant(input, 20)
        return listOf()
    }

    fun indexing(chunks: List<Document>): Boolean {
//        val addAll = store.addAll(chunks)
        return true
    }
}

enum class StoreType {
    Elasticsearch
}

fun apply(name: String, init: Workflow.() -> Unit): Workflow {
    val workflow = Workflow(name)
    workflow.init()
    return workflow
}
