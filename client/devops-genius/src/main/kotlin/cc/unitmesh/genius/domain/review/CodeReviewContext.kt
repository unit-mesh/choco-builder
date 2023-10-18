package cc.unitmesh.genius.domain.review

data class CodeReviewContext(
    var businessContext: String = "",
    var fullMessage: String = "",
    var changes: String = "",
)