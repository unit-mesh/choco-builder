package cc.unitmesh.genius.domain.review

import cc.unitmesh.genius.prompt.PromptFactory

class CodeReviewPromptFactory(public override var context: CodeReviewContext = CodeReviewContext()) :
    PromptFactory("code-review") {
    override val templatePath = "simple-review.open-ai.vm"
}
