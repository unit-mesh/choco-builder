package cc.unitmesh.genius.devops;

import cc.unitmesh.genius.devops.kanban.GitHubKanban
import cc.unitmesh.genius.devops.kanban.GitlabKanban
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class KanbanServiceTest {

    @Test
    fun should_return_GitHubKanban_when_given_valid_github_url() {
        // given
        val url = "https://github.com/example/repository"

        // when
        val result = KanbanFactory.fromRepositoryUrl(url, "token")

        // then
        result!!.javaClass shouldBe GitHubKanban::class.java
    }

    @Test
    fun should_return_GitlabKanban_when_given_valid_gitlab_url() {
        // given
        val url = "https://gitlab.com/example/repository"

        // when
        val result = KanbanFactory.fromRepositoryUrl(url, "token")

        // then
        result!!.javaClass shouldBe GitlabKanban::class.java
    }
}