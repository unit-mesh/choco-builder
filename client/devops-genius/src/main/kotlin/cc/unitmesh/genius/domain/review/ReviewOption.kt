package cc.unitmesh.genius.domain.review

import cc.unitmesh.genius.project.GeniusProject

class ReviewOption(
    override val path: String = "",
    override val verbose: Boolean,
    val repo: String,
    val branch: String,
    val sinceCommit: String,
    val untilCommit: String,
    val commitOptionFile: String,
    val project: GeniusProject,
) : ActionOption(
    path = path,
    verbose = verbose,
) {
}