package cc.unitmesh.cf.factory.process

import cc.unitmesh.cf.factory.base.Answer
import cc.unitmesh.cf.factory.dsl.Dsl
import cc.unitmesh.cf.factory.dsl.Interpreter

abstract class SolutionExecutor(
    private val interpreters: List<Interpreter>
) {
    abstract fun execute(solution: Dsl): Answer
}