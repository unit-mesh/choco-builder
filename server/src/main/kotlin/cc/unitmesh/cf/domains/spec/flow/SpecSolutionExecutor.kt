package cc.unitmesh.cf.domains.spec.flow

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.cf.core.dsl.Interpreter
import cc.unitmesh.cf.core.flow.SolutionExecutor
import cc.unitmesh.cf.core.flow.model.Answer
import cc.unitmesh.cf.core.llms.LlmProvider
import io.reactivex.rxjava3.core.Flowable


data class SpecLang(
    val words: List<String>,
    override val content: String,
) : Dsl {
    override var domain: String = "spec"
    override var interpreters: List<DslInterpreter> = listOf()
}

// TODO: add support for solution executor
class SpecSolutionExecutor(
    private val completion: LlmProvider
): SolutionExecutor<SpecLang> {
    override val interpreters: List<Interpreter> = listOf()

    override fun execute(solution: SpecLang): Flowable<Answer> {
        TODO("Not yet implemented")
    }
}