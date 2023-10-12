package cc.unitmesh.cf.code;

import org.junit.jupiter.api.Test

class GitOutputTest {

    @Test
    fun should_return_latest_commit_hash() {
        val gitCommand = GitCommand()

        val hash = gitCommand.latestCommitHash()
        println(hash)
    }
}