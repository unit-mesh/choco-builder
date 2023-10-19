package cc.unitmesh.genius.domain.review

import cc.unitmesh.cf.code.GitDiffer
import cc.unitmesh.genius.context.GeniusAction
import cc.unitmesh.genius.devops.Issue
import cc.unitmesh.genius.project.GeniusProject
import org.changelog.CommitParser
import org.slf4j.LoggerFactory

class CodeReviewAction(
    val project: GeniusProject,
    private val option: ReviewOption,
    private val diff: GitDiffer,
    private val commitParser: CommitParser,
) : GeniusAction {
    private val promptFactory = CodeReviewPromptFactory()
    private val context = CodeReviewContext()

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
            try {
                project.fetchStory(it.issue)
            } catch (e: Exception) {
                logger.error("fetch story error: $it", e)
                null
            }
        }.filterNotNull()

        context.businessContext = stories.joinToString(System.lineSeparator(), transform = Issue::title)

        val patch = diff.patchBetween(option.sinceCommit, option.untilCommit)
        context.changes = patch.filter {
            project.commitLog?.isIgnoreFile(it.key) ?: true
        }.map {
            it.value.content
        }.joinToString(System.lineSeparator())

        promptFactory.context = context
        val messages = promptFactory.createPrompt(project, "")

        logger.info("messages: $messages")

        project.connector().streamCompletion(messages).blockingForEach {
            print(it)
        }

        return ""
    }

    companion object {
        val logger = LoggerFactory.getLogger(CodeReviewAction::class.java)
    }
}
