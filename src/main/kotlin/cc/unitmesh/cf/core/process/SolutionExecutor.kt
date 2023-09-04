package cc.unitmesh.cf.core.process

import cc.unitmesh.cf.core.base.Answer
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.Interpreter

typealias ExecuteResult = Answer;

interface SolutionExecutor<in T: Dsl> {
    val interpreters: List<Interpreter>
    fun execute(solution: T): Answer
}