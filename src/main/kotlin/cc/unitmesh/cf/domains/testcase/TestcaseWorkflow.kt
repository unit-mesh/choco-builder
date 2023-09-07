package cc.unitmesh.cf.domains.testcase

import cc.unitmesh.cf.core.prompt.UpdatableExample
import cc.unitmesh.cf.core.workflow.StageContext
import cc.unitmesh.cf.core.workflow.Workflow
import cc.unitmesh.cf.core.workflow.WorkflowResult
import cc.unitmesh.cf.infrastructure.llms.completion.TemperatureMode
import cc.unitmesh.cf.presentation.domain.ChatWebContext

class TestcaseWorkflow : Workflow() {
    override val prompts: LinkedHashMap<StageContext.Stage, StageContext> = linkedMapOf(
        CLARIFY.stage to CLARIFY,
        ANALYZE.stage to ANALYZE,
        DESIGN.stage to DESIGN,
    )

    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): WorkflowResult? {
        TODO("Not yet implemented")
    }

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(TestcaseWorkflow::class.java)
        val CLARIFY: StageContext = StageContext(
            id = "Testcase Analyse",
            stage = StageContext.Stage.Clarify,
            systemPrompt = """你是一个世界级的质量工程师（Quality assurance），职责是帮助用户识别测试要点，并定义测试目标。
                |要求如下：
                |- 当用户提供的信息不足，你必须提出问题，直到你能够识别测试要点，并定义测试目标。
                |- 当用户的问题比较发散、不明确，请和用户沟通，收集更多的信息，帮助用户明确他们的要点。
                |- 当用户的需求太大，你必须建议用户拆解成多个场景，并建议用户一个个完成。
                |
                |你应该遵循以下的方法：
                |
                |- 基于测试目标，识别系统的各种使用场景和情境。考虑不同用户、操作流程、输入数据等因素。
                |- 将每个场景进一步分解成具体的测试步骤和预期结果。确保每个场景都可以清晰地分解为可执行的测试用例。
                |- 在场景设计过程中，特别关注边界条件和极端情况，因为这些情况通常容易导致问题。确保测试包括了这些边界条件。
                |- 对场景和测试用例进行分类，根据功能的重要性和影响确定测试的优先级。优先测试关键功能，然后再测试次要功能。
                |
            """.trimMargin(),
            updatableExamples = listOf(
                UpdatableExample(
                    question = "用户登录功能",
                    answer = """测试目标：
                        |- 验证用户可以使用有效的用户名和密码成功登录到系统。
                        |- 验证用户在使用无效的用户名或密码时无法登录。
                        |- 验证系统是否正确处理特殊字符的用户名和密码。
                    """.trimMargin(),
                    userResponse = "提供的边界条件太少了，能不能再添加一些。",
                    finalOutput = """测试目标：
                        |- 验证用户可以使用有效的用户名和密码成功登录到系统。
                        |- 验证用户在使用无效的用户名或密码时无法登录。
                        |- 验证系统是否正确处理特殊字符的用户名和密码。
                        |- 验证系统对密码长度的限制。
                        |- 验证系统是否记录登录失败的尝试次数，并在一定次数后锁定账户。
                    """.trimMargin()
                ),
                UpdatableExample(
                    question = "商品管理",
                    answer = """您想要编写的测试用例功能场景太大，建议拆分为多个场景，再往下进行：商品上架、商品编辑、商品删除、商品检索、商品库存管理、商品排序等。""".trimMargin(),
                    userResponse = "商品管理-商品上架",
                    finalOutput = """测试目标：
                        | - 验证管理员可以成功将商品上架到系统，并能显示已上架的商品。
                        | - 验证系统能正确处理商品的上架日期和状态。
                        | - 验证系统能够正确处理商品上架的价格和库存信息。
                        | - 验证系统能够正确处理上架商品的图片上传和显示。
                        | - 验证系统能够正确处理上架商品的分类和标签。
                        | - 验证系统能够正确计算和显示上架商品的销售统计信息。
                    """.trimMargin()
                )
            ),
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
