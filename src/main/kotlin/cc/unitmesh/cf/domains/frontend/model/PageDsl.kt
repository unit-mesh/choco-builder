package cc.unitmesh.cf.domains.frontend.model

import cc.unitmesh.cf.core.SubDomain
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslBase
import com.fasterxml.jackson.annotation.JsonGetter

@SubDomain(name = "页面", description = "某个页面的 DSL 描述")
data class PageDsl(
    @get:JsonGetter("类型")
    val type: String,

    @get:JsonGetter("名称")
    val name: String,

    @get:JsonGetter("布局方式")
    val layouts: List<LayoutStyle>,

    @get:JsonGetter("组件")
    val components: List<ComponentDsl>,
) : Dsl {
    override var domain: String = ""

    override lateinit var interpreters: List<DslBase>
}