package cc.unitmesh.cf.factory.context

import cc.unitmesh.cf.factory.dsl.DslInterpreterContext

abstract class DslContextBuilder {
    abstract fun buildFor(domain: DslInterpreterContext, question: String, chatHistories: String = ""): DslContext
}