package cc.unitmesh.cf.core.context

import cc.unitmesh.cf.core.dsl.InterpreterContext
import java.time.LocalDate

abstract class DslContext(
    /**
     * 最相关的 interpreter
     */
    val nearestInterpreters: List<InterpreterContext>,
    val chatHistories: String,
) {
}