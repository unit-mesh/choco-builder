package cc.unitmesh.cf.core.dsl

interface Dsl {
    var domain: String

    /**
     * the Display content for ChatUI
     */
    val content: String
    var interpreters: List<DslInterpreter>
}
