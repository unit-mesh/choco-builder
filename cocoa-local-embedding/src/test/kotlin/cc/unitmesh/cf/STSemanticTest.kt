package cc.unitmesh.cf

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class STSemanticTest {

    @Test
    fun embed_demo() {
        val semantic = STSemantic.create()
        val embedding = semantic.embed("what is the weather today?")

        embedding.size shouldBe 3072
    }
}