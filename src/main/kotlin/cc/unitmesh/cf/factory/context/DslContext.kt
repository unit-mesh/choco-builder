package cc.unitmesh.cf.factory.context

import cc.unitmesh.cf.factory.dsl.InterpreterContext
import java.time.LocalDate

abstract class DslContext(
    /**
     * 最相关的 interpreter
     */
    val nearestInterpreters: List<InterpreterContext>,
    val chatHistories: String,
    val time: LocalDate,
) {
}