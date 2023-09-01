package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.core.process.SubDomainDeclaration

class FEDomain : SubDomainDeclaration {
    override val name: String get() = "Frontend"
    override val description: String get() = "设计前端 UI，生成前端代码、组件等"

    override fun matchQuestion(question: String): List<String> {
        //
        return listOf()
    }
}