package cc.unitmesh.cf.domains.frontend

import cc.unitmesh.cf.factory.dsl.Dsl
import cc.unitmesh.cf.factory.dsl.DslBase
import com.fasterxml.jackson.annotation.JsonGetter

/**
 * Design system basic dsl
 * // https://ant.design/docs/spec/font-cn
 */
data class DesignSystemDsl(
    @get:JsonGetter("色彩")
    val color: BrandColor,

    @get:JsonGetter("字体家族")
    val fontFamily: List<String>,

    @get:JsonGetter("字体风格")
    val fontStyles: List<FontStyle>,

    @get:JsonGetter("布局方式")
    val layouts: List<LayoutStyle>,
) : Dsl {
    override var domain: String = ""

    override lateinit var interpreters: List<DslBase>
}

data class BrandColor(
    @get:JsonGetter("主色")
    val primary: String,
    @get:JsonGetter("辅助色")
    val secondary: String,
    @get:JsonGetter("成功色")
    val success: String,
    @get:JsonGetter("警告色")
    val warning: String,
    @get:JsonGetter("危险色")
    val danger: String,
    @get:JsonGetter("信息色")
    val info: String,
    @get:JsonGetter("文本色")
    val text: String,
    @get:JsonGetter("背景色")
    val background: String,
    @get:JsonGetter("边框色")
    val border: String,
)

enum class FontStyles(val displayName: String) {
    TITLE("标题"),
    PRIMARY_TEXT("一级文本"),
    SECONDARY_TEXT("二级文本"),
    DISABLED_TEXT("禁用文本"),
    PRIMARY_BORDER("一级边框"),
    SEPARATOR("分割线"),
    LAYOUT_BACKGROUND("布局背景"),
}

data class FontStyle(
    @get:JsonGetter("类型")
    val type: FontStyles,
    @get:JsonGetter("字体色")
    val fontColor: String,
    @get:JsonGetter("字体大小")
    val fontSize: String,
    @get:JsonGetter("字体粗细")
    val fontWeight: Double,
)

data class LayoutStyle(
    @get:JsonGetter("类型")
    val type: String,
    @get:JsonGetter("说明")
    val description: String,
    @get:JsonGetter("样式")
    val style: String,
)