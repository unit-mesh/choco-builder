package cc.unitmesh.cf.code;

import cc.unitmesh.cf.code.command.OS
import cc.unitmesh.cf.code.command.getOS
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GitOutputTest {

    @Test
    fun should_return_latest_commit_hash() {
        val gitCommand = GitCommand()

        val hash = gitCommand.latestCommitHash()
        getOS()?.let { os ->
            when {
                os != OS.WINDOWS -> {
                    hash.stdout.split("\n").size shouldBe 10
                }
                else -> {
                    println(hash.stdout)
                    // maybe WSL ?
//                    hash.stdout.split("\r\n").size shouldBe 10
                }
            }
        }
    }
}