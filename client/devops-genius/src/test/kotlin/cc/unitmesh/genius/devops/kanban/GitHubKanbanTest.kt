package cc.unitmesh.genius.devops.kanban;

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class GitHubKanbanTest {

    @Test
    fun should_formatUrl_correctly() {
        // given
        val repoUrl = "https://github.com/unitmesh/devti"
        val expectedFormattedUrl = "unitmesh/devti"

        // when
        val formattedUrl = GitHubKanban.formatUrl(repoUrl)

        // then
        assertEquals(expectedFormattedUrl, formattedUrl)
    }
}