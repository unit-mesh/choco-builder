package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.core.process.DomainDeclaration
import cc.unitmesh.cf.core.prompt.Workflow
import org.springframework.stereotype.Component

@Component
class FEDomainDecl() : DomainDeclaration {
    override val domainName: String get() = "frontend"
    override val description: String get() = "设计前端 UI，生成前端代码、组件等"

    override fun workflow(question: String): Workflow {
        return FEWorkflow()
    }
}