package cc.unitmesh.cf.factory.process

import cc.unitmesh.cf.factory.dsl.Dsl

interface QuestionAnalyzer {
    fun analyze(domain: String, question: String): Dsl
}