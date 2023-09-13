package cc.unitmesh.cf.domains.spec

import cc.unitmesh.cf.core.flow.DomainDeclaration
import cc.unitmesh.cf.core.flow.Workflow
import org.springframework.stereotype.Component

@Component
class SpecDomainDecl : DomainDeclaration {
    override val domainName: String get() = "spec"
    override val description: String get() = "查询系统相关的规范等。"

    override fun workflow(question: String): Workflow {
        return SpecWorkflow()
    }
}