package cc.unitmesh.rag.document;

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LostInMiddleReorderTest {

    @Test
    fun should_return_reordered_list() {
        // given
        val documents = listOf(
            Pair(0.6, "doc1"),
            Pair(0.5, "doc2"),
            Pair(0.4, "doc3"),
            Pair(0.3, "doc4"),
            Pair(0.2, "doc5"),
            Pair(0.1, "doc6")
        )

        // when
        val result = DocumentOrder.lostInMiddleReorder(documents)

        val expected = listOf(
            0.5 to "doc2",
            0.3 to "doc4",
            0.1 to "doc6",
            0.2 to "doc5",
            0.4 to "doc3",
            0.6 to "doc1"
        )
        assertEquals(expected, result)
    }
}