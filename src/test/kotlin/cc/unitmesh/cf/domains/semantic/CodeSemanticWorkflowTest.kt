package cc.unitmesh.cf.domains.semantic

import kotlin.test.Test

class CodeSemanticWorkflowTest {
    @Test
    fun should_output_analysis_prompt_for_testing() {
        val context = CodeSemanticWorkflow.ANALYSIS
        println(context.format())
    }
}