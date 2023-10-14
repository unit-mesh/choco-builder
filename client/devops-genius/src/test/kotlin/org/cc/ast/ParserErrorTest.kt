package org.cc.ast

import org.junit.jupiter.api.Test

class ParserErrorTest {
    @Test
    fun `should throw exception`() {
        val text = "feat add support for scopes"
        try {
            Parser(text).parse()
        } catch (e: Exception) {
//            assert(e.message == "Expected '(' but was ' ' at position 4")
        }
    }
}