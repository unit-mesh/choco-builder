package cc.unitmesh.cf.core.process

import cc.unitmesh.cf.core.base.FlowActionFlag


typealias ClarifyResult = Pair<FlowActionFlag, String>;

/**
 *
 */
interface ProblemClarifier {
    fun clarify(domain: String, question: String, histories: List<String> = emptyList()): Pair<FlowActionFlag, String>
}