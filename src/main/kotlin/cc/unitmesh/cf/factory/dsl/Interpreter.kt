package cc.unitmesh.cf.factory.dsl

interface Interpreter {
    fun canInterpret(dsl: Dsl): Boolean

    fun interpret(dsl: Dsl): String
}
