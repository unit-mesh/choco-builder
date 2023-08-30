package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.core.base.ClarificationAction
import cc.unitmesh.cf.core.process.QuestionClarifier

class FEQuestionClarifier: QuestionClarifier {
    override fun clarify(
        domain: String,
        question: String,
        historyMessages: List<String>,
    ): Pair<ClarificationAction, String> {
        TODO("Not yet implemented")
    }
}