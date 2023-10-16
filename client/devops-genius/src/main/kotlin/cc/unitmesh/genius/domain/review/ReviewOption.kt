package cc.unitmesh.genius.domain.review

class ReviewOption(
    val path: String = "",
    val verbose: Boolean,
    val repo: String,
    val branch: String,
    val sinceCommit: String,
    val untilCommit: String,
    val commitOptionFile: String,
) {
}