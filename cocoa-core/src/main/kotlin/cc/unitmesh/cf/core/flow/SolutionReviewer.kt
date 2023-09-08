package cc.unitmesh.cf.core.flow

import cc.unitmesh.cf.core.dsl.Dsl

interface SolutionReviewer {
    fun review(question: String, histories: List<String> = emptyList()): Dsl
}