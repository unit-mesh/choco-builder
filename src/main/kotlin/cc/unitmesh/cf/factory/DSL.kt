package cc.unitmesh.cf.factory

interface DSL {
    var domain: String

    var interpreters: List<DslInterpreter>
}
