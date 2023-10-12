package cc.unitmesh.cf.code;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GitOutputTest {

    @Test
    fun should_return_latest_commit_hash() {
        val gitCommand = GitCommand()

        val hash = gitCommand.latestCommitHash()
        hash.stdout.split("\n").size shouldBe 10
    }
}