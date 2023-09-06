package cc.unitmesh.cf.domains.spring

import cc.unitmesh.cf.core.process.DomainDeclaration
import cc.unitmesh.cf.core.workflow.Workflow
import org.springframework.stereotype.Component

@Component
class SpringDomainDecl : DomainDeclaration {
    override val domainName: String get() = "spring"
    override val description: String get() = "生成后端 Spring 应用代码，如 API Swagger 等等"

    override fun workflow(question: String): Workflow {
        return SpringWorkflow()
    }
}