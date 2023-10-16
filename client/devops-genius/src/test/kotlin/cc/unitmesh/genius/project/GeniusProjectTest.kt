package cc.unitmesh.genius.project;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GeniusProjectTest {

    @Test
    fun `fromYml should return GeniusProject object`() {
        // given
        val yaml = """
name: "ChocolateFactory"
path: "." # relative to the project root, or GitHub repo, like "unitmesh/chocolate-factory"

# store the changelog in the repo
store:
  indexName: "unitmesh/chocolate-factory" # default to github repo

kanban:
  type: GitHub
  token: "xx"
        """.trimIndent()

        // when
        val geniusProject = GeniusProject.fromYml(yaml)

        // then
        geniusProject.name shouldBe "ChocolateFactory"
        geniusProject.path shouldBe "."
    }
}