package cc.unitmesh.cf.core.context

import cc.unitmesh.cf.core.dsl.InterpreterContext

abstract class DslContext(
    /**
     * 最相关的 interpreter
     */
    val nearestInterpreters: List<InterpreterContext>,
    val chatHistories: String,
) {
    /**
     * resolve variables in prompts, to make them more human-friendly
     * should consider use [cc.unitmesh.cf.core.context.variable.VariableResolver] to resolve variables
     */
    abstract fun compileVariable(text: String): String
}