package cc.unitmesh.cf.domains.frontend.model

import cc.unitmesh.cf.core.SubDomain
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.cf.core.dsl.IndexElement
import cc.unitmesh.cf.infrastructure.parser.MarkdownCode
import cc.unitmesh.cf.infrastructure.utils.uuid
import com.fasterxml.jackson.annotation.JsonGetter

@SubDomain(name = "页面", description = "某个页面的 DSL 描述")
data class UiPage(
    override val id: String = uuid(),

    @get:JsonGetter("名称")
    override val name: String,

    @get:JsonGetter("布局")
    val layout: String,

    @get:JsonGetter("组件")
    val components: List<UiComponent> = listOf(),
) : Dsl, IndexElement {

    override var domain: String = "frontend"
    override var interpreters: List<DslInterpreter> = listOf()

    companion object {
        /**
         * 解析页面 DSL
         * example:
         *
         * output:请确认以下的设计是否符合您的要求。如果符合，请回复"YES"，如果不符合，请提出你的要求。
         * ```design
         * pageName: 聊天详细页
         * ----------------------------------------------
         * |      Navigation(10x)                       |
         * ----------------------------------------------
         * | Empty(2x) | ChatHeader(8x) | Empty(2x) |
         * ----------------------------------------------
         * | MessageList(10x)                         |
         * ----------------------------------------------
         * | MessageInput(10x)                        |
         * ----------------------------------------------
         * | Footer(10x)                                |
         * ----------------------------------------------
         * ```
         *
         * 请确认以上设计是否符合您的要求。如果需要进行任何修改或调整，请提出您的具体要求。
         */
        fun parse(string: String): UiPage {
            val code = MarkdownCode.parse(string);
            if (code.language != "design") {
                throw IllegalArgumentException("不支持的语言: ${code.language}")
            }

            val lines = code.text.lines()
            val pageName = lines[0].substringAfter(":").trim()
            val layout = lines.subList(1, lines.size).joinToString("\n")
            return UiPage(name = pageName, layout = layout)
        }
    }
}
