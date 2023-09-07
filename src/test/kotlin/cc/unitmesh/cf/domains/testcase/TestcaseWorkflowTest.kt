package cc.unitmesh.cf.domains.testcase

import org.junit.jupiter.api.Test

class TestcaseWorkflowTest {
    @Test
    fun should_output_prompt_for_testing() {
        println(TestcaseWorkflow.ANALYZE.format());
    }
}