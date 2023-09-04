package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.core.process.DomainDeclaration
import cc.unitmesh.cf.core.prompt.Workflow
import cc.unitmesh.cf.domains.frontend.context.FEDslContextBuilder
import cc.unitmesh.cf.domains.frontend.context.FEVariableResolver
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider
import org.springframework.stereotype.Component

@Component
class FEDomainDecl(
    private val contextBuilder: FEDslContextBuilder,
    private val completion: LlmProvider,
    private val variable: FEVariableResolver,
) : DomainDeclaration {
    override val domainName: String get() = "frontend"
    override val description: String get() = "设计前端 UI，生成前端代码、组件等"

    override fun workflow(question: String): Workflow {
        return FEWorkflow(contextBuilder, completion, variable)
    }
}