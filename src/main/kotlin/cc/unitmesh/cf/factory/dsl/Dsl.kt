package cc.unitmesh.cf.factory.dsl

interface Dsl {
    var domain: String

    var interpreters: List<DslBase>
}
