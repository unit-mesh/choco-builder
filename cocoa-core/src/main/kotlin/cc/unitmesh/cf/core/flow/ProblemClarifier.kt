package cc.unitmesh.cf.core.flow

import cc.unitmesh.cf.core.flow.model.FlowActionFlag


typealias ClarifyResult = Pair<FlowActionFlag, String>;

/**
 *
 */
interface ProblemClarifier {
    fun clarify(domain: String, question: String, histories: List<String> = emptyList()): Pair<FlowActionFlag, String>
}