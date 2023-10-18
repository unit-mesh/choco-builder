package cc.unitmesh.genius.domain.review

import cc.unitmesh.cf.code.GitDiffer
import cc.unitmesh.genius.context.GeniusAction
import cc.unitmesh.genius.devops.Issue
import cc.unitmesh.genius.project.GeniusProject
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.changelog.CommitParser
import org.slf4j.LoggerFactory

class CodeReviewAction(
    val project: GeniusProject,
    val option: ReviewOption,
    val diff: GitDiffer,
    val commitParser: CommitParser,
) : GeniusAction {
    val promptFactory = CodeReviewPromptFactory()
    val context = CodeReviewContext()

    override fun execute(): Any {
        val commitMessages = diff.commitMessagesBetween(option.sinceCommit, option.untilCommit)
        context.fullMessage = commitMessages.map { it.value }.joinToString(System.lineSeparator())

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
            logger.info("commit don't need review")
        }

        val storyIds = parsedMsgs.map { it.references }.flatten()
        val stories = storyIds.map {
            project.fetchStory(it.issue)
        }
        logger.info("stories: $stories")

        context.businessContext = stories.joinToString(System.lineSeparator(), transform = Issue::title)

        val patch = diff.patchBetween(option.sinceCommit, option.untilCommit)
        context.changes = patch.filter {
            project.commitLog?.isIgnoreFile(it.key) ?: true
        }.map {
            it.value
        }.joinToString(System.lineSeparator())

        promptFactory.context = context
        val prompt = promptFactory.createPrompt(project, "")
        println(prompt)

        return ""
    }

    companion object {
        val logger = LoggerFactory.getLogger(CodeReviewAction::class.java)
    }
}
