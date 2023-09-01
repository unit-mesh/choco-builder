package cc.unitmesh.cf.core.process

import cc.unitmesh.cf.core.dsl.Dsl

interface SolutionDesigner {
    fun design(domain: String, question: String, histories: List<String> = emptyList()): Dsl
}