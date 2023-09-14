package cc.unitmesh.cf.domains.spec.flow

import cc.unitmesh.cf.core.dsl.Interpreter
import cc.unitmesh.cf.core.flow.SolutionExecutor
import cc.unitmesh.cf.core.flow.model.Answer
import cc.unitmesh.cf.core.llms.LlmProvider
import cc.unitmesh.cf.domains.spec.context.SpecVariableResolver
import cc.unitmesh.cf.domains.spec.model.SpecLang
import io.reactivex.rxjava3.core.Flowable

class SpecSolutionExecutor(
    private val completion: LlmProvider,
    private val variable: SpecVariableResolver,
): SolutionExecutor<SpecLang> {
    override val interpreters: List<Interpreter> = listOf()

    override fun execute(solution: SpecLang): Flowable<Answer> {
        TODO("Not yet implemented")
    }
}