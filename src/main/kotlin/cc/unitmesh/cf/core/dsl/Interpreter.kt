package cc.unitmesh.cf.core.dsl

interface Interpreter {
    fun canInterpret(dsl: Dsl): Boolean

    fun interpret(dsl: Dsl): Any
}
