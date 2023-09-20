package cc.unitmesh.apply

import cc.unitmesh.apply.base.ApplyDsl
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.rag.document.Document
import cc.unitmesh.rag.document.DocumentOrder
import cc.unitmesh.rag.store.EmbeddingMatch


class Llm(val prompt: String) {
    fun run() {
        println("llm $prompt")
    }

    fun request(function: () -> Unit) {

    }
}

enum class EngineType {
    SentenceTransformers,
    TextEmbeddingAda,
}

class EmbeddingEngine(val engine: EngineType = EngineType.SentenceTransformers) {
    fun query(input: String) : List<EmbeddingMatch<Document>> {
        return listOf()
    }

    fun indexing(chunks: List<Document>): Boolean {
        return true
    }
}

class DocumentDsl(val file: String) {
    fun split() : List<Document> {
        return listOf()
    }

}

/**
 * Apply is a DSL for invoking a function in a template.
 */
@ApplyDsl
class Workflow(val name: String) {
    var connection: LlmProvider? = null

    fun llm(prompt: String): Llm {
        return Llm(prompt)
    }

    fun embedding(engineType: EngineType = EngineType.SentenceTransformers): EmbeddingEngine {
        return EmbeddingEngine(engineType)
    }

    fun document(file: String): DocumentDsl {
        return DocumentDsl(file)
    }
}

fun apply(name: String, init: Workflow.() -> Unit): Workflow {
    val workflow = Workflow(name)
    workflow.init()
    return workflow
}
