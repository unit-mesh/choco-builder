package cc.unitmesh.cf.domains.code

import cc.unitmesh.cf.core.process.DomainDeclaration
import cc.unitmesh.cf.core.workflow.Workflow
import cc.unitmesh.cf.domains.SupportedDomains
import org.springframework.stereotype.Component

@Component
class CodeInterpreterDomainDecl : DomainDeclaration {
    override val domainName: String get() = SupportedDomains.CodeInterpreter.value
    override val description: String get() = "生成和执行 Kotlin 代码，如生成 API、绘图等。"

    override fun workflow(question: String): Workflow {
        return CodeInterpreterWorkflow()
    }
}