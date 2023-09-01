package cc.unitmesh.cf.core.process.impl

import cc.unitmesh.cf.core.process.DomainDeclaration
import cc.unitmesh.cf.core.prompt.DefaultWorkflow
import cc.unitmesh.cf.core.prompt.Workflow
import org.springframework.stereotype.Component

@Component
class DomainDeclarationPlaceholder : DomainDeclaration {
    override val domainName: String get() = "Default"
    override val description: String get() = "根据问题判断领域"

    override fun workflow(question: String): Workflow {
        return DefaultWorkflow()
    }
}
