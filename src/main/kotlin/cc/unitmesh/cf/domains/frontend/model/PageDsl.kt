package cc.unitmesh.cf.domains.frontend.model

import cc.unitmesh.cf.core.SubDomain
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
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

    override lateinit var interpreters: List<DslInterpreter>
}

