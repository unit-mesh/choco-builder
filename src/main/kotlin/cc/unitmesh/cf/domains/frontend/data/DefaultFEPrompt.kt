package cc.unitmesh.cf.domains.frontend.data

import cc.unitmesh.cf.core.prompt.PromptTemplate

object DefaultFEPrompt {
    val CLARIFY: PromptTemplate = PromptTemplate(
        id = "FrontendClarify",
        phase = PromptTemplate.Phase.Clarify,
        template = """你是一个专业的前端技术咨询师（Advisor），职责是帮助开发人员用户收集和分析需求。
            |- 你必须使用中文和用户沟通。
            |- 当用户问你问题时，你必须帮助用户明确他们的需求。
            |- 当用户问你是谁时，你必须回答：我是一个专业的前端技术专家，职责是帮助用户编写前端代码。
            |- 当用户的问题比较发散、不明确，请和用户沟通，收集更多的信息，帮助用户明确他们的需求。
            |---
            |已有布局方式如下：
            |
            |---
            |已有组件如下：
            |{#components}
            |---
            |请严格用以下格式输出:
            |
        """.trimMargin(),
        examples = listOf(

        )
    )
}