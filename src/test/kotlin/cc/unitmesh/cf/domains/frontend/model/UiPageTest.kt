package cc.unitmesh.cf.domains.frontend.model;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UiPageTest {

    @Test
    fun should_throwIllegalArgumentException_when_languageIsNotDesign() {
        val string = "```kotlin\n" +
                "fun test() {\n" +
                "    println(\"Hello, World!\")\n" +
                "}\n" +
                "```"

        assertThrows<IllegalArgumentException> { UiPage.parse(string) }
    }

    @Test
    fun should_returnPageDsl_when_languageIsDesign() {
        val string = """```design
pageName: 聊天详细页
usedComponents: PageHeader, Empty, ChatHeader, MessageList, Input, Button, Footer
----------------------------------------------
|      Navigation(10x)                       |
----------------------------------------------
| Empty(2x) | ChatHeader(8x) | Empty(2x) |
----------------------------------------------
| MessageList(10x)                         |
----------------------------------------------
| MessageInput(10x)                        |
----------------------------------------------
| Footer(10x)                                |
----------------------------------------------
```"""

        // when
        val result = UiPage.parse(string)

        // then
//        assertEquals("聊天详细页", result.name)
        result.name shouldBe "聊天详细页"
        result.components shouldBe listOf(
            "PageHeader",
            "Empty",
            "ChatHeader",
            "MessageList",
            "Input",
            "Button",
            "Footer"
        )

        assertEquals(
            """----------------------------------------------
|      Navigation(10x)                       |
----------------------------------------------
| Empty(2x) | ChatHeader(8x) | Empty(2x) |
----------------------------------------------
| MessageList(10x)                         |
----------------------------------------------
| MessageInput(10x)                        |
----------------------------------------------
| Footer(10x)                                |
----------------------------------------------""", result.layout
        )
    }
}