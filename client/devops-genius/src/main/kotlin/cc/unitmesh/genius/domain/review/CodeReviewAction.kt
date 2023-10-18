package cc.unitmesh.genius.domain.review

import cc.unitmesh.cf.code.GitDiffer
import cc.unitmesh.genius.context.GeniusAction
import cc.unitmesh.genius.project.GeniusProject
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.changelog.CommitParser

class CodeReviewAction(
    val project: GeniusProject,
    val option: ReviewOption,
    private val diff: GitDiffer,
    private val commitParser: CommitParser,
) : GeniusAction {
    override fun execute(): Any {
        val commitMessages = diff.commitMessagesBetween(option.sinceCommit, option.untilCommit)
        val parsedMsgs = commitMessages.map {
            commitParser.parse(it.value)
        }

        val filterCommits = parsedMsgs.filter {
            if (it.meta.containsKey("type")) {
                val type = it.meta["type"] as String
                project.commitLog?.isIgnoreType(type) ?: true
            } else {
                true
            }
        }

        if (option.verbose) {
            println("parsedMsgs: $parsedMsgs")
            println("filterCommits: $filterCommits")
        }

        if (filterCommits.isEmpty()) {
            println("commit don't need review")
        }

        val storyIds = parsedMsgs.map { it.references }.flatten()
        val stories = storyIds.map {
            project.fetchStory(it.issue)
        }
        println("stories: $stories")

        val patch = diff.patchBetween(option.sinceCommit, option.untilCommit)
        patch.filter {
            project.commitLog?.isIgnoreFile(it.key) ?: true
        }.map {
            println("path: ${it.key}")
            println(it.value)
        }

        val callList = diff.countBetween(option.sinceCommit, option.untilCommit)
        println(Json.encodeToString(callList))

        return ""
    }

}