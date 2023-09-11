package cc.unitmesh.dsl.design

import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class DesignDslTest {
    @Test
    fun should_parse_basic_dsl() {
        val dsl = """Layout Navigation {
--------------------------------------
| "home" |"detail" | Button("Login") |
--------------------------------------
}"""

        val design = DesignDsl().analysis(dsl)

        design.layouts.size shouldBe 1
        design.layouts[0].layoutName shouldBe "Navigation"

        design.layouts[0].layoutRows.size shouldBe 1

        val firstRow = design.layouts[0].layoutRows[0]

        firstRow.layoutCells.size shouldBe 3
        firstRow.layoutCells[0].componentName shouldBe "home"
        firstRow.layoutCells[1].componentName shouldBe "detail"
        firstRow.layoutCells[2].componentName shouldBe "Button"
    }

    @Test
    fun should_success_parse_flow() {
        val dsl = """flow login {
    SEE HomePage
    DO [Click] "Login".Button
        REACT Success: SHOW "Login Success".Toast with ANIMATE(bounce)
        REACT Failure: SHOW "Login Failure".Dialog

    SEE "Login Failure".Dialog
    DO [Click] "ForgotPassword".Button
        REACT: GOTO ForgotPasswordPage

    SEE ForgotPasswordPage
    DO [Click] "RESET PASSWORD".Button
        REACT: SHOW "Please Check Email".Message
}"""

        val design = DesignDsl().analysis(dsl)

        design.flows.size shouldBe 1
        design.flows[0].interactions.size shouldBe 3

        val firstInteraction = design.flows[0].interactions[0]
        firstInteraction.see.componentName shouldBe "HomePage"
        firstInteraction.`do`.componentName shouldBe "Button"
        firstInteraction.`do`.uiEvent shouldBe "Click"
        firstInteraction.react.size shouldBe 2

        firstInteraction.react[0].sceneName shouldBe "Success"
        firstInteraction.react[0].reactAction shouldBe "SHOW"
        firstInteraction.react[0].reactComponentName shouldBe "Toast"
        firstInteraction.react[0].animateName shouldBe "bounce"
        firstInteraction.react[0].reactComponentData shouldBe "Login Success"
    }

    @Test
    fun should_correct_parse_components() {
        val dsl = """component Navigation {
    LayoutId: Navigation
}

component TitleComponent {}
component ImageComponent {
    Size: 1080px
}
component BlogList {
    BlogDetail, Space8
    BlogDetail, Space8
    BlogDetail, Space8
    BlogDetail, Space8
    BlogDetail, Space8
    BlogDetail, Space8
}
"""

        val design = DesignDsl().analysis(dsl)

        design.components.size shouldBe 4
        design.components["Navigation"]!!.name shouldBe "Navigation"
        design.components["TitleComponent"]!!.name shouldBe "TitleComponent"
        design.components["ImageComponent"]!!.name shouldBe "ImageComponent"
        design.components["BlogList"]!!.name shouldBe "BlogList"

        design.components["BlogList"]!!.child.size shouldBe 12
        design.components["BlogList"]!!.child[0].name shouldBe "BlogDetail"
        design.components["BlogList"]!!.child[1].name shouldBe "Space8"
    }

    @Test
    fun should_parse_library() {
        val dsl = """library FontSize {
    H1 = 18px
    H2 = 16px
    H3 = 14px
    H4 = 12px
    H5 = 10px
}

library Color {
    Primary {
        label = "Primary"
        value = "#E53935"
    }
    Secondary {
        label = "Blue"
        value = "#1E88E5"
    }
}

library Button {
    Default [
        FontSize.H2, Color.Primary
    ]
    Primary [
        FontSize.H2, Color.Primary
    ]
}"""

        val design = DesignDsl().analysis(dsl)

        design.libraries.size shouldBe 3
        design.libraries[0].name shouldBe "FontSize"
        design.libraries[0].presets.size shouldBe 5
        design.libraries[0].presets[0].key shouldBe "H1"
        design.libraries[0].presets[0].value shouldBe "18px"
        design.libraries[0].presets[1].key shouldBe "H2"
        design.libraries[0].presets[1].value shouldBe "16px"
        design.libraries[0].presets[2].key shouldBe "H3"
        design.libraries[0].presets[2].value shouldBe "14px"
        design.libraries[0].presets[3].key shouldBe "H4"
        design.libraries[0].presets[3].value shouldBe "12px"
        design.libraries[0].presets[4].key shouldBe "H5"
        design.libraries[0].presets[4].value shouldBe "10px"

        design.libraries[1].name shouldBe "Color"
        design.libraries[1].presets.size shouldBe 2
        val libraryPreset = design.libraries[1].presets[0]
        libraryPreset.key shouldBe "Primary"

        libraryPreset.subProperties.size shouldBe 2
        libraryPreset.subProperties[0].key shouldBe "label"
        libraryPreset.subProperties[0].value shouldBe "Primary"
        libraryPreset.subProperties[1].key shouldBe "value"
        libraryPreset.subProperties[1].value shouldBe "#E53935"

        design.libraries[2].name shouldBe "Button"
        design.libraries[2].presets.size shouldBe 2
        design.libraries[2].presets[0].key shouldBe "Default"
        design.libraries[2].presets[0].inheritProps.size shouldBe 2

        design.libraries[2].presets[0].inheritProps[0].key shouldBe "FontSize"
        design.libraries[2].presets[0].inheritProps[0].value shouldBe "H2"
    }

    @Test
    fun simple_dsl_for_layout() {
        val dsl = """------------------------------------------------------
|      NavComponent(12x)                             |
------------------------------------------------------
|    Text(6x)                     |   Empty(6x)      |
------------------------------------------------------
| Avatar(3x)  | Date(3x)          |   Empty(6x)      |
------------------------------------------------------
| CardMedia(8x)                   |   Empty(4x)      |
------------------------------------------------------
| Typography(12x)                                    |
------------------------------------------------------
| FooterComponent(12x)                               |
------------------------------------------------------"""

        val design = DesignDsl().analysis(dsl)
        design.layouts[0].layoutRows.size shouldBe 6
    }

    @Test
    fun ignore_unknown_component() {
        val dsl = """pageName: 博客详情页
usedComponents: Grid, Box, NavComponent, Avatar, Date, CardMedia, Typography, Pagination, FooterComponent
------------------------------------------------------
|      NavComponent(12x)                             |
------------------------------------------------------
|    Text(6x)                     |   Empty(6x)      |
------------------------------------------------------
| Avatar(3x)  | Date(3x)          |   Empty(6x)      |
------------------------------------------------------
| CardMedia(8x)                   |   Empty(4x)      |
------------------------------------------------------
| Typography(12x)                                    |
------------------------------------------------------
| FooterComponent(12x)                               |
------------------------------------------------------
""".trimIndent()

        val design = DesignDsl().analysis(dsl)
        design.layouts[0].layoutRows.size shouldBe 6
    }

    @Test
    fun should_handle_for_error_case() {
        val dsl = """-------------------------------------------------------------------------------------
|      NavComponent(12x)                                                            |
-------------------------------------------------------------------------------------
|    TextField(12x)                                                                 |
-------------------------------------------------------------------------------------
|    TextField(12x)                                                                 |
-------------------------------------------------------------------------------------
|    TextField(6x)                            |   Button("存为草稿")(6x)           |
-------------------------------------------------------------------------------------
|    Button("发布")(12x)                                                           |
-------------------------------------------------------------------------------------
"""

    }
}
