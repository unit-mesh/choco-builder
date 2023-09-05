package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.core.prompt.*
import cc.unitmesh.cf.core.workflow.StageContext
import cc.unitmesh.cf.core.workflow.Workflow
import cc.unitmesh.cf.core.workflow.WorkflowResult
import cc.unitmesh.cf.domains.frontend.context.FEDslContextBuilder
import cc.unitmesh.cf.domains.frontend.context.FEVariableResolver
import cc.unitmesh.cf.domains.frontend.flow.FEProblemClarifier
import cc.unitmesh.cf.infrastructure.llms.completion.LlmProvider
import cc.unitmesh.cf.presentation.domain.ChatWebContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class FEWorkflow() : Workflow() {
    @Autowired
    private lateinit var contextBuilder: FEDslContextBuilder

    @Autowired
    private lateinit var variableResolver: FEVariableResolver

    @Autowired
    private lateinit var llmProvider: LlmProvider


    override val prompts: LinkedHashMap<StageContext.Stage, StageContext>
        get() = linkedMapOf(
            CLARIFY.stage to CLARIFY,
            DESIGN.stage to DESIGN,
            EXECUTE.stage to EXECUTE
        )

    override fun execute(prompt: StageContext, chatWebContext: ChatWebContext): WorkflowResult? {
        val messages = chatWebContext.messages

        when (prompt.stage) {
            StageContext.Stage.Classify -> throw IllegalStateException("Frontend workflow should not be used for classify")
            StageContext.Stage.Clarify -> {
                FEProblemClarifier(contextBuilder, llmProvider, variableResolver).clarify(
                    domain = "frontend",
                    question = messages[0].content,
                    histories = messages.map { it.content }
                )
            }

            StageContext.Stage.Analyze -> TODO()
            StageContext.Stage.Design -> TODO()
            StageContext.Stage.Execute -> TODO()
            StageContext.Stage.Custom -> TODO()
        }

        return null
    }

    companion object {
        val CLARIFY: StageContext = StageContext(
            id = "FrontendClarify",
            stage = StageContext.Stage.Clarify,
            systemPrompt = """你是一个专业的前端技术咨询师（Advisor），职责是帮助开发人员用户收集和分析需求。
            |- 你必须使用中文和用户沟通。
            |- 当用户问你问题时，你必须帮助用户明确他们的需求。
            |- 当用户问你是谁时，你必须回答：我是一个专业的前端技术专家，职责是帮助用户编写前端代码。
            |- 当用户的问题比较发散、不明确，请和用户沟通，收集更多的信息，帮助用户明确他们的需求。
            |---
            |
            |已有布局方式如下：
            |${'$'}{layouts}
            |---
            |
            |请严格用以下格式输出:
            |思考：是否包含了用户故事，页面元素的描述和布局信息，如果明确请结束询问，如果不明确，请继续询问。
            |行动：为"CONTINUE"或者"FINISH"
            |询问：想继续询问的问题
            |最终输出：完整的问题
            |思考-行动-询问可以循环数次，直到最终输出
            |
            |示例一：
            |输入：编写一个登录页面
            |输出：
            |思考：用户想要编写登录页面，但是没有说明页面元素的描述和布局信息，应该继续询问。
            |行动：CONTINUE
            |询问：您想要什么布局方式？
            |
            |示例二：
            |输入：编写一个栅格布局的登录页面，需要包含用户名、密码输入框和登录按钮
            |输出：
            |思考：用户想要编写栅格布局的登录页面，其中栅格布局是布局方式，登录页面是用户故事，页面元素包含用户名、密码输入框和登录按钮，已经明确，应该结束询问。
            |行动：FINISH
            |最终输出：编写一个栅格布局的登录页面
            |
            |以下是您和当前用户的交互历史：${'$'}{history}
        """.trimMargin()
        )

        /**
         * UI 布局 DSL 基于： [https://github.com/phodal/design](https://github.com/phodal/design)
         */
        val DESIGN: StageContext = StageContext(
            id = "FrontendDesign",
            stage = StageContext.Stage.Design,
            systemPrompt = """你是一个专业的前端技术咨询师（Advisor），请以如下的 ASCII 描述用户所需要的页面。
            |
            |- 如果用户没有给出页面元素的描述，你必须自己补充。
            |- 你必须等待用户确认，确认后才能继续。
            | 
            |所有的组件列表如下：
            |###
            |${'$'}{components}
            |###
            |
            |如下是基本的 ASCII 规则，以便用户以程序解析它：
            |
            |- a(), p() 以小写字母开头的函数，表示页面元素
            |- Footer(10x),BlogList(10x) 以大写字母开头的函数，表示页面组件
            |- Empty(2x) 表示空白, 2x 表示页面元素的宽度为 2x栅格宽度
            |- Navigation(10x) 表示导航栏, 10x 表示页面元素的宽度为 10x栅格宽度
            |
        """.trimMargin(),
            updateExamples = listOf(
                QAUpdateExample(
                    question = "生成一个导航栏的 mockup",
                    answer = """请确认以下的设计是否符合您的要求。如果符合，请回复"YES"，如果不符合，请提出你的要求。
            |```design
            |pageName: 导航栏
            |--------------------------------------
            || a("home") | p("博客") | p("Login")  |
            |--------------------------------------
            |```
                """.trimMargin(),
                    nextAction = "这里的 login 应该是 button，而不是 a",
                    finalOutput = """
            |```design
            |pageName: 导航栏
            |--------------------------------------
            || a("home") | p("博客") | button("Login")  |
            |--------------------------------------
            |```
                """.trimMargin()
                ),
                QAUpdateExample(
                    question = "生成一个包含图片的博客列表面 mockup",
                    answer = """请确认以下的设计是否符合您的要求。如果符合，请回复"YES"，如果不符合，请提出你的要求。
            |```design
            |pageName: 博客列表
            |----------------------------------------------
            ||      Navigation(10x)                       |
            |----------------------------------------------
            || Empty(2x) | TitleComponent(6x) | Empty(2x) |
            |----------------------------------------------
            || BlogList(8x)           | Archives(2x)      |
            |----------------------------------------------
            || Footer(10x)                                |
            |----------------------------------------------
            |```
                """.trimMargin(),
                    nextAction = "YES",
                )
            )
        )
        val EXECUTE: StageContext = StageContext(
            id = "FrontendExecute",
            stage = StageContext.Stage.Execute,
            systemPrompt = """你是一个资深的前端开发人员，帮助编写用户设计好的前端 UI。你需要根据下面的需求和页面，生成对应的前端代码。
            |- 项目的技术栈是 React + TypeScript + Ant Design。
            |
            |###
            |请根据用户提供的问题，生成前端代码。
            |###
            |
            |用户提供的问题：
            |${'$'}{question}
            |
            |业务布局：
            |${'$'}{layout}
        """.trimMargin(),
            examples = listOf(
                QAExample(
                    question = "",
                    answer = ""
                )
            )
        )
    }
}