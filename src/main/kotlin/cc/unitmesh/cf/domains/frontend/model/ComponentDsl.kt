package cc.unitmesh.cf.domains.frontend.model

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslBase
import cc.unitmesh.cf.core.prompt.CoTExample
import com.fasterxml.jackson.annotation.JsonGetter
import kotlinx.serialization.Serializable

@Serializable
data class ComponentDsl(
    @get:JsonGetter("类型")
    val type: String,

    @get:JsonGetter("名称")
    val name: String,

    @get:JsonGetter("说明")
    val description: String,

    @get:JsonGetter("属性")
    val properties: List<Property> = emptyList(),

    @get:JsonGetter("事件")
    val events: List<Event> = emptyList(),

    @get:JsonGetter("方法")
    val methods: List<String> = emptyList(),

    @get:JsonGetter("示例")
    val examples: List<CoTExample> = emptyList(),
) : Dsl {
    override var domain: String = ""

    override var interpreters: List<DslBase> = emptyList()
}

@Serializable
data class Property(
    @get:JsonGetter("参数") val parameter: String,
    @get:JsonGetter("说明") val description: String,
    @get:JsonGetter("类型") val type: String,
    @get:JsonGetter("可选值") val optionalValue: String,
    @get:JsonGetter("默认值") val defaultValue: String,
)

@Serializable
data class Event(
    @get:JsonGetter("事件名称") val eventName: String,
    @get:JsonGetter("说明") val description: String,
    @get:JsonGetter("回调参数") val callbackParameter: String,
)
