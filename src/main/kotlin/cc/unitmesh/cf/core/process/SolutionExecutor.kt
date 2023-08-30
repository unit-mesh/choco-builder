package cc.unitmesh.cf.core.process

import cc.unitmesh.cf.core.base.Answer
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.Interpreter

abstract class SolutionExecutor(
    private val interpreters: List<Interpreter>
) {
    abstract fun execute(solution: Dsl): Answer
}