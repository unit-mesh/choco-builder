package cc.unitmesh.cf.domains.frontend.model

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.cf.core.dsl.IndexElement
import cc.unitmesh.cf.core.prompt.QAExample
import cc.unitmesh.cf.core.utils.IdUtil
import com.fasterxml.jackson.annotation.JsonGetter
import kotlinx.serialization.Serializable

@Serializable
data class UiComponent(
    override val id: String = IdUtil.uuid(),
    override val name: String,

    // like: <div>, <Button>
    val tagName: String,
    val components: List<String>,
    val description: String,
    val examples: List<ComponentExample> = emptyList(),
) : Dsl, IndexElement {
    override var domain: String = ""
    override val content: String get() = ""

    override var interpreters: List<DslInterpreter> = emptyList()
}

@Serializable
data class ComponentExample(
    val name: String,
    val content: String,
)

@Serializable
data class UiComponentStandard(
    override val id: String = IdUtil.uuid(),

    @get:JsonGetter("名称")
    override val name: String,

    @get:JsonGetter("标签名称")
    val tagName: String,

    @get:JsonGetter("类型")
    val type: String,

    @get:JsonGetter("说明")
    val description: String,

    @get:JsonGetter("属性")
    val properties: List<Property> = emptyList(),

    @get:JsonGetter("事件")
    val events: List<Event> = emptyList(),

    @get:JsonGetter("方法")
    val methods: List<String> = emptyList(),

    @get:JsonGetter("示例")
    val examples: List<QAExample> = emptyList(),
) : Dsl, IndexElement {
    override var domain: String = ""
    override val content: String get() = ""

    override var interpreters: List<DslInterpreter> = emptyList()
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
