package cc.unitmesh.apply

import cc.unitmesh.apply.base.ApplyDsl
import cc.unitmesh.rag.document.Document


class Llm(val prompt: String) {
    fun run() {
        println("llm $prompt")
    }

    fun request(function: () -> Unit) {

    }
}

enum class EngineType {
    SentenceTransformers,
    Ada,
}

class EmbeddingEngine(val input: String, val engine: EngineType = EngineType.SentenceTransformers) {
    fun search() {

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
    fun llm(prompt: String): Llm {
        return Llm(prompt)
    }

    fun embedding(input: String, engineType: EngineType = EngineType.SentenceTransformers): EmbeddingEngine {
        return EmbeddingEngine(input, engineType)
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
