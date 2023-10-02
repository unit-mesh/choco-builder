package cc.unitmesh.prompt.model;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class VariableTest {

    @Test
    fun should_return_range_with_integer_step() {
        val range = Variable.Range("key", "0~100", "1")
        val result: ClosedRange<BigDecimal> = range.toRange()
        assertEquals(0.toBigDecimal().rangeTo(100.toBigDecimal()), result)
    }
}
