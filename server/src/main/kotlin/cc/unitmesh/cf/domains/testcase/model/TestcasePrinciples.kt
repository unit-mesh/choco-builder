package cc.unitmesh.cf.domains.testcase.model

import cc.unitmesh.cf.core.prompt.QAExample

data class TestcasePrinciples(
    val name: String,
    val description: String,
    val examples: List<QAExample> = listOf()
) {
}