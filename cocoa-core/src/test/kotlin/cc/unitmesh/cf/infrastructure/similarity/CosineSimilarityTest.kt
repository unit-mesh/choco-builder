package cc.unitmesh.cf.infrastructure.similarity

import cc.unitmesh.cf.core.nlp.similarity.CosineSimilarity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CosineSimilarityTest {
    @Test
    @DisplayName("完全相同的向量距离应该为0")
    fun shouldBeZeroDistanceForIdenticalVectors() {
        val similarity = CosineSimilarity()
        assertEquals(similarity.similarityScore(listOf(1.0, 2.0, 3.0), listOf(1.0, 2.0, 3.0)), 0.0)
    }

    @Test
    @DisplayName("平行的向量距离应该为0")
    fun shouldBeZeroDistanceForParallelVectors() {
        val similarity = CosineSimilarity()
        assertEquals(similarity.similarityScore(listOf(1.0, 2.0, 3.0), listOf(2.0, 4.0, 6.0)), 0.0)
    }

    @Test
    @DisplayName("不同向量的距离应该非零")
    fun shouldBeNonZeroDistanceForParallelVectors() {
        val similarity = CosineSimilarity()
        assertEquals(similarity.similarityScore(listOf(1.0, 2.0, 3.0), listOf(1.0, 2.0, 4.0)), 0.008539866016332498)
    }

    @Test
    @DisplayName("向量差异越大则距离应该越大")
    fun shouldBeGreaterForSignificantlyDifferentVectors() {
        val similarity = CosineSimilarity()
        val distance1 = similarity.similarityScore(listOf(1.0, 2.0, 3.0), listOf(1.0, 2.0, 4.0))
        val distance2 = similarity.similarityScore(listOf(1.0, 2.0, 3.0), listOf(3.0, 2.0, 5.0))

        assertThat(distance2).isGreaterThan(distance1)
    }
}