package cc.unitmesh.genius.domain.review

import cc.unitmesh.genius.context.ActionOption
import cc.unitmesh.genius.project.GeniusProject

class ReviewOption(
    val path: String = "",
    val verbose: Boolean,
    val repo: String,
    val branch: String,
    val sinceCommit: String,
    val untilCommit: String,
    val commitOptionFile: String,
    val project: GeniusProject,
) : ActionOption {

}