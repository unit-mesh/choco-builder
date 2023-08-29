package cc.unitmesh.cf.domains.frontend.model

import cc.unitmesh.cf.factory.dsl.Dsl
import cc.unitmesh.cf.factory.dsl.DslBase
import com.fasterxml.jackson.annotation.JsonGetter

data class FEComponentDsl(
    @get:JsonGetter("类型")
    val type: String,

    @get:JsonGetter("名称")
    val name: String,

    @get:JsonGetter("属性")
    val properties: List<Property>,

    @get:JsonGetter("事件")
    val events: List<Event>,

    @get:JsonGetter("方法")
    val methods: List<String>,
) : Dsl {
    override var domain: String = ""

    override lateinit var interpreters: List<DslBase>
}

data class Property(
    @get:JsonGetter("参数") val parameter: String,
    @get:JsonGetter("说明") val description: String,
    @get:JsonGetter("类型") val type: String,
    @get:JsonGetter("默认值") val defaultValue: String,
)

data class Event(
    @get:JsonGetter("事件名称") val parameter: String,
    @get:JsonGetter("说明") val description: String,
    @get:JsonGetter("回调参数") val callbackParameter: String,
)
