package cc.unitmesh.cf.core.flow

import cc.unitmesh.cf.core.dsl.Dsl

typealias DesignResult = Dsl;

interface SolutionDesigner {
    fun design(domain: String, question: String, histories: List<String> = emptyList()): Dsl
}