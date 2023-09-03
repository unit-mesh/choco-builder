package cc.unitmesh.cf.core.process

import cc.unitmesh.cf.core.base.ClarificationAction

interface ProblemClarifier {
    fun clarify(domain: String, question: String, histories: List<String> = emptyList()): Pair<ClarificationAction, String>
}