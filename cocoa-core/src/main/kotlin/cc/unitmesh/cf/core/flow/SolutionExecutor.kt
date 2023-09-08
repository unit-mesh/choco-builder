package cc.unitmesh.cf.core.flow

import cc.unitmesh.cf.core.flow.model.Answer
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.Interpreter

typealias ExecuteResult = Answer;

interface SolutionExecutor<in T: Dsl> {
    val interpreters: List<Interpreter>
    fun execute(solution: T): Answer
}