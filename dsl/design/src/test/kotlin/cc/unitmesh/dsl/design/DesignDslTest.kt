package cc.unitmesh.dsl.design

import io.kotest.matchers.shouldBe
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
}