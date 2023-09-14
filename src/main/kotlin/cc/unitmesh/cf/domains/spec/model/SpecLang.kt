package cc.unitmesh.cf.domains.spec.model

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter

data class SpecLang(
    val words: List<String>,
    override val content: String,
) : Dsl {
    override var domain: String = "spec"
    override var interpreters: List<DslInterpreter> = listOf()
}