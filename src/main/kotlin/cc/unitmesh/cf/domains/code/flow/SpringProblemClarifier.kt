package cc.unitmesh.cf.domains.code.flow

import cc.unitmesh.cf.core.base.FlowActionFlag
import cc.unitmesh.cf.core.process.ProblemClarifier
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider

class SpringProblemClarifier(
    private val completion: LlmProvider,
) : ProblemClarifier {
    override fun clarify(domain: String, question: String, histories: List<String>): Pair<FlowActionFlag, String> {
        TODO("Not yet implemented")
    }
}
