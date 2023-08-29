package cc.unitmesh.cf.domains.openapi

import cc.unitmesh.cf.factory.dsl.Dsl
import cc.unitmesh.cf.factory.dsl.DslBase
import com.fasterxml.jackson.annotation.JsonGetter

data class OpenApiDsl(
    @get:JsonGetter("名称")
    val name: String,

    @get:JsonGetter("说明")
    val description: String,

    @get:JsonGetter("版本")
    val version: String,

    @get:JsonGetter("路径")
    val paths: List<String>,

    @get:JsonGetter("标签")
    val tags: List<String>,
) : Dsl {
    override var domain: String = ""

    override lateinit var interpreters: List<DslBase>
}
