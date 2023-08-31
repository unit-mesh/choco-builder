package cc.unitmesh.cf.domains.frontend.prompt

import cc.unitmesh.cf.core.prompt.CoTExample
import cc.unitmesh.cf.core.prompt.PromptTemplate
import cc.unitmesh.cf.core.prompt.Workflow

class FEWorkflow : Workflow() {
    override val prompts: LinkedHashMap<PromptTemplate.Phase, PromptTemplate>
        get() = linkedMapOf(
            CLARIFY.phase to CLARIFY,
            ANALYZE.phase to ANALYZE,
            DESIGN.phase to DESIGN,
            EXECUTE.phase to EXECUTE
        )

    val CLARIFY: PromptTemplate = PromptTemplate(
        id = "FrontendClarify",
        phase = PromptTemplate.Phase.Clarify,
        systemPrompt = """你是一个专业的前端技术咨询师（Advisor），职责是帮助开发人员用户收集和分析需求。
            |- 你必须使用中文和用户沟通。
            |- 当用户问你问题时，你必须帮助用户明确他们的需求。
            |- 当用户问你是谁时，你必须回答：我是一个专业的前端技术专家，职责是帮助用户编写前端代码。
            |- 当用户的问题比较发散、不明确，请和用户沟通，收集更多的信息，帮助用户明确他们的需求。
            |---
            |
            |已有布局方式如下：
            |{layouts}
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
            |以下是您和当前用户的交互历史：{交互历史}
        """.trimMargin()
    )
    val ANALYZE: PromptTemplate = PromptTemplate(
        id = "FrontendAnalyse",
        phase = PromptTemplate.Phase.Analyze,
        systemPrompt = """你是一个专业的前端技术咨询师（Advisor），请以如下的 ASCII 描述用户所需要的页面。
            | - 如果用户没有给出页面元素的描述，请自行补充。
            | - 你需要等待用户确认，确认后才能继续。
            | 
            |请按如上的 ASCII格式输出，以便用户以程序解析它：
            |```text
            |// a(), p() 以小写字母开头的函数，表示页面元素
            |// Footer,BlogList 以大写字母开头的函数，表示页面组件
            |// Empty(2x) 表示空白, 2x 表示页面元素的宽度为 2x栅格宽度
            |```
        """.trimMargin(),
        examples = listOf(
            CoTExample(
                question = "生成一个导航栏的 mockup",
                answer = """
            |--------------------------------------
            || a("home") | p("博客") | p("Login")  |
            |--------------------------------------                     
                """.trimMargin()
            ),
            CoTExample(
                question = "生成一个包含图片的博客列表面 mockup",
                answer = """
            |----------------------------------------------
            ||      Navigation(10x)                       |
            |----------------------------------------------
            || Empty(2x) | TitleComponent(6x) | Empty(2x) |
            |----------------------------------------------
            || BlogList(8x)           | Archives(2x)      |
            |----------------------------------------------
            || Footer(10x)                                |
            |----------------------------------------------
                """.trimMargin()
            )
        )
    )
    val DESIGN: PromptTemplate = PromptTemplate(
        id = "FrontendDesign",
        phase = PromptTemplate.Phase.Design,
        systemPrompt = """""",
        examples = listOf(
            CoTExample(
                question = "",
                answer = ""
            )
        )
    )
    val EXECUTE: PromptTemplate = PromptTemplate(
        id = "FrontendExecute",
        phase = PromptTemplate.Phase.Execute,
        systemPrompt = """""",
        examples = listOf(
            CoTExample(
                question = "",
                answer = ""
            )
        )
    )
}