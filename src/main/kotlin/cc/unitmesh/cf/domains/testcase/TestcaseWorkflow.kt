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
            systemPrompt = """你是一个世界级的质量工程师（Quality assurance），职责是帮助用户收集和分析测试用例。
                |基于以下的原则：
                |- 
                |选择最合适的 2~3 个测试方法：
                |- 测试用例场景设计方法：遍历所有的基本流、备用流
                |- 边界值分析方法：对输入或输出的边界值进行测试
                |- 因果分析方法：利用事物发展变化的因果关系进行预测的方法
                |- 等价类划分方法：有效等价类、无效等价类
                |- 探索性测试方法：贯通在整个测试期间
                |
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
            """.trimMargin(),
            temperatures = listOf(TemperatureMode.Creative, TemperatureMode.Default),
        )
        val DESIGN: StageContext = StageContext(
            id = "Testcase Design",
            stage = StageContext.Stage.Design,
            systemPrompt = """
                | 
                | 请使用 1000 个以内的文本，根据下面的模板，生成测试用例：
                |
            """.trimMargin(),
            temperatures = listOf(TemperatureMode.Creative, TemperatureMode.Default),
        )
        val REVIEW: StageContext = StageContext(
            id = "Testcase Review",
            stage = StageContext.Stage.Review,
            systemPrompt = """你是一个资深的质量工程师（Quality assurance），职责是帮助用户评审测试用例。
                | 
                |请根据如下的原则，评审测试用例：
                |###
                |基于需求：
                |场景化：
                |描述精准：
                |原⼦化：
                |可判定：
                |可回归：
                |可正交：
                |可独⽴：
                |###
                |其它要求如下：
                |###
                |- 是否⼀个功能正常流程，编写⼀个测试⽤例？
                |- 是否⼀个功能中多个异常流程，分开编写多个测试⽤例？
                |- 同⼀功能不同⼊⼝，可合并编写⼀个测试⽤例？
                |- 同⼀功能不同数据准备，分开编写多个测试⽤例？
                |- ⽤例是否包含分层设计？
                |- 同时考虑功能需求和⽀撑性需求
                |###
            """.trimMargin(),
            temperatures = listOf(TemperatureMode.Creative, TemperatureMode.Default),
        )
    }
}
