package cc.unitmesh.prompt.model;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VariableTest {

    @Test
    fun should_return_range_with_integer_step() {
        val range = Variable.Range("key", "0~100", "1")
        val result = range.toRange()
        assertEquals(0.0..100.0, result)
    }
}
