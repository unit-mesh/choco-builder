package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.process.SolutionDesigner
import cc.unitmesh.cf.domains.frontend.dsl.FEDslContextBuilder
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider

class FESolutionDesigner(
    private val contextBuilder: FEDslContextBuilder,
    private val completion: LlmProvider,
) : SolutionDesigner {
    override fun design(domain: String, question: String, histories: List<String>): Dsl {
        TODO("Not yet implemented")
    }
}