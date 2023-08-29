package cc.unitmesh.cf.factory

interface Interpreter {
    fun canInterpret(dsl: DSL): Boolean

    fun interpret(dsl: DSL): String
}
