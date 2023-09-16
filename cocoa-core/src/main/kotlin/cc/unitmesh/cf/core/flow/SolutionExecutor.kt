package cc.unitmesh.cf.core.flow

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.Interpreter
import cc.unitmesh.cf.core.flow.model.Answer
import io.reactivex.rxjava3.core.Flowable

typealias ExecuteResult = Answer;

interface SolutionExecutor<in T: Dsl> {
    val interpreters: List<Interpreter>
    fun execute(solution: T): Flowable<Answer>
}