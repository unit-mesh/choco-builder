package cc.unitmesh.cf.domains.semantic

import cc.unitmesh.cf.core.flow.DomainDeclaration
import cc.unitmesh.cf.core.flow.Workflow
import cc.unitmesh.cf.domains.SupportedDomains
import org.springframework.stereotype.Component

@Component
class CodeSemanticDecl : DomainDeclaration {
    override val domainName: String get() = SupportedDomains.CodeSemanticSearch.value
    override val description: String get() = "语义化的代码搜索，以帮助你更好的理解代码库。"

    override fun workflow(question: String): Workflow {
        return CodeSemanticWorkflow()
    }
}