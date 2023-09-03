package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.core.base.Answer
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.Interpreter
import cc.unitmesh.cf.core.process.SolutionExecutor

class FESolutionExecutor : SolutionExecutor {
    override val interpreters: List<Interpreter> = listOf()
    override fun execute(solution: Dsl): Answer {
        TODO("Not yet implemented")
    }
}