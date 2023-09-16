package cc.unitmesh.cf.core.flow.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class FlowActionFlagTest {

    @Test
    fun should_return_continue_action_and_ask_content() {
        // given
        val content = "行动:CONTINUE\n询问: What is your name?"

        // when
        val result = FlowActionFlag.parse(content)

        // then
        assertEquals(FlowActionFlag.CONTINUE, result.first)
        assertEquals("What is your name?", result.second)
    }

    @Test
    fun should_return_finish_action_and_output_content() {
        // given
        val content = "行动:FINISH\n最终输出: Thank you!"

        // when
        val result = FlowActionFlag.parse(content)

        // then
        assertEquals(FlowActionFlag.FINISH, result.first)
        assertEquals("Thank you!", result.second)
    }

    @Test
    fun should_throw_exception_when_action_not_found() {
        // given
        val content = "行动:InvalidAction\n询问: What is your name?"

        // when-then
        assertThrows(RuntimeException::class.java) {
            FlowActionFlag.parse(content)
        }
    }
}