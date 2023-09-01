package cc.unitmesh.cf.domains.frontend.model

import cc.unitmesh.cf.core.SubDomain
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.cf.infrastructure.parser.MarkdownCode
import com.fasterxml.jackson.annotation.JsonGetter

@SubDomain(name = "页面", description = "某个页面的 DSL 描述")
data class PageDsl(
    @get:JsonGetter("名称")
    val pageName: String,

    @get:JsonGetter("布局")
    val layout: String,

    @get:JsonGetter("组件")
    val components: List<ComponentDsl> = listOf(),
) : Dsl {

    override var domain: String = "frontend"
    override var interpreters: List<DslInterpreter> = listOf()

    // output:请确认以下的设计是否符合您的要求。如果符合，请回复"YES"，如果不符合，请提出你的要求。
    //```design
    //pageName: 聊天详细页
    //----------------------------------------------
    //|      Navigation(10x)                       |
    //----------------------------------------------
    //| Empty(2x) | ChatHeader(8x) | Empty(2x) |
    //----------------------------------------------
    //| MessageList(10x)                         |
    //----------------------------------------------
    //| MessageInput(10x)                        |
    //----------------------------------------------
    //| Footer(10x)                                |
    //----------------------------------------------
    //```
    //
    //请确认以上设计是否符合您的要求。如果需要进行任何修改或调整，请提出您的具体要求。
    companion object {
        fun parse(string: String): PageDsl {
            val code = MarkdownCode.parse(string);
            if (code.language != "design") {
                throw IllegalArgumentException("不支持的语言: ${code.language}")
            }

            val lines = code.text.lines()
            val pageName = lines[0].substringAfter(":").trim()
            val layout = lines.subList(1, lines.size).joinToString("\n")
            return PageDsl(pageName, layout)
        }
    }
}

