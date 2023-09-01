package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.core.Domain
import cc.unitmesh.cf.core.process.DomainDeclaration

@Domain(name = "前端子域", description = "生成前端代码等")
class FEDomain : DomainDeclaration {
    override fun matchQuestion(question: String): List<String> {
        //
        return listOf()
    }
}