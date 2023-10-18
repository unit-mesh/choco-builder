package cc.unitmesh.genius.domain.review;

import cc.unitmesh.genius.project.GeniusProject
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test;

class CodeReviewPromptFactoryTest {
    @Test
    fun should_success_create_prompt() {
        val codeReviewPromptFactory = CodeReviewPromptFactory()
        val prompt = codeReviewPromptFactory.createPrompt(GeniusProject(), "test")
        prompt shouldNotBe null
    }
}