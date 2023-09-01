package cc.unitmesh.cf.infrastructure.similarity;

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JaccardSimilarityTest {
    @Test
    fun should_calculate_similarity_score() {
        val similarity = JaccardSimilarity()
        // given
        val a = listOf(1.0, 2.0, 3.0)
        val b = listOf(1.0, 2.0, 4.0)

        // when
        val score = similarity.similarityScore(a, b)

        // then
        assertEquals(0.5, score)
    }

}
