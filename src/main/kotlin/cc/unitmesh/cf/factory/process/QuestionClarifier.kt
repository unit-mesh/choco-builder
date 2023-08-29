package cc.unitmesh.cf.factory.process

import cc.unitmesh.cf.factory.base.ClarificationAction

interface QuestionClarifier {
    fun clarify(domain: String, question: String, historyMessages: List<String> = emptyList()): Pair<ClarificationAction, String>
}