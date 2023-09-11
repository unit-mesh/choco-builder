package cc.unitmesh.cf.core.dsl

interface DslCompiler {
    fun compile(dsl: Dsl): Any
}