package cc.unitmesh.cf.domains.testcase

import cc.unitmesh.cf.core.workflow.StageContext
import cc.unitmesh.cf.core.workflow.Workflow
import cc.unitmesh.cf.core.workflow.WorkflowResult
import cc.unitmesh.cf.infrastructure.llms.completion.TemperatureMode
import cc.unitmesh.cf.presentation.domain.ChatWebContext

class TestcaseWorkflow : Workflow() {
    override val prompts: LinkedHashMap<StageContext.Stage, StageContext> = linkedMapOf(
        CLASSIFY.stage to CLASSIFY,
        ANALYZE.stage to ANALYZE,
        DESIGN.stage to DESIGN,
    )

    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): WorkflowResult? {
        TODO("Not yet implemented")
    }

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(TestcaseWorkflow::class.java)
        val CLASSIFY: StageContext = StageContext(
            id = "Testcase Analyse",
            stage = StageContext.Stage.Classify,
            systemPrompt = """
                |基于以下的原则：
                |- 
                |选择最合适的 2~3 个测试方法：
                |- 测试用例场景设计方法：遍历所有的基本流、备用流
                |- 边界值分析方法：对输入或输出的边界值进行测试
                |- 因果分析方法：利用事物发展变化的因果关系进行预测的方法
                |- 等价类划分方法：有效等价类、无效等价类
                |- 探索性测试方法：贯通在整个测试期间
            """.trimMargin(),
            temperatures = listOf(TemperatureMode.Creative, TemperatureMode.Default),
        )

        val ANALYZE: StageContext = StageContext(
            id = "Testcase Analyse",
            stage = StageContext.Stage.Analyze,
            systemPrompt = """
                | 
                | 请使用 1000 个以内的文本，根据下面的模板，生成测试用例：
                |
            """.trimIndent(),
            temperatures = listOf(TemperatureMode.Creative, TemperatureMode.Default),
        )

        val DESIGN: StageContext = StageContext(
            id = "Testcase Design",
            stage = StageContext.Stage.Design,
            systemPrompt = """
                | 
                | 请使用 1000 个以内的文本，根据下面的模板，生成测试用例：
                |
            """.trimIndent(),
            temperatures = listOf(TemperatureMode.Creative, TemperatureMode.Default),
        )
    }
}
