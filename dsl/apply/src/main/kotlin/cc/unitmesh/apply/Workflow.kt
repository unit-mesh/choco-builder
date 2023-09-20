package cc.unitmesh.apply

import cc.unitmesh.apply.base.ApplyDsl
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.llms.LlmProvider

/**
 * Apply is a DSL for invoking a function in a template.
 */
@ApplyDsl
class Workflow(val name: String) {
    var connection: LlmProvider? = null
    var dsl: Dsl? = null;

    fun llm(prompt: String): Llm {
        return Llm(prompt)
    }

    fun embedding(engineType: EngineType = EngineType.SentenceTransformers): EmbeddingEngine {
        return EmbeddingEngine(engineType)
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

/**
 * for create LlmProvider
 */
fun Connection(connectorName: String): LlmProvider {
    TODO("Not yet implemented")
}

fun apply(name: String, init: Workflow.() -> Unit): Workflow {
    val workflow = Workflow(name)
    workflow.init()
    return workflow
}
