package cc.unitmesh.prompt.model;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RangeTest {

    @Test
    fun `should return range with integer step`() {
        // given
        val range = Variable.Range("key", "0~100", "1")

        // when
        val result = range.toRange()

        // then
        assertEquals(0.0..100.0, result)
    }
}