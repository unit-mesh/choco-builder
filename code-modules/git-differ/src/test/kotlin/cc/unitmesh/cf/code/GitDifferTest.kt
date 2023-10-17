package cc.unitmesh.cf.code;

import cc.unitmesh.cf.code.command.OS
import cc.unitmesh.cf.code.command.getOS
import io.kotest.matchers.shouldBe
import kotlin.io.path.Path
import kotlin.test.Ignore
import kotlin.test.Test

class GitDifferTest {
    @Test
    fun should_enable_to_diff() {
        if (getOS() == OS.WINDOWS) {
            return
        }

        val gitCommand = GitCommand()

        val hashes: List<String> = gitCommand.latestCommitHash().stdout.split("\n")

        val path = Path(".").toAbsolutePath().parent.parent
        val gitDiffer = GitDiffer(path.toString(), "master")

        val changedNodes = gitDiffer.patchBetween(hashes[0], hashes[1])
        println(changedNodes)
    }

    @Test
    fun should_trim_diff_string() {
        if (getOS() == OS.WINDOWS) {
            return
        }

        // given
        val diffString = """
        diff --git a/file1.txt b/file1.txt
        index 1234567..abcdefg 100644
        --- a/file1.txt (revision 1234567)
        +++ b/file1.txt (revision abcdefg)
        @@ -1,2 +1,2 @@
        -Hello
        +Hi
        -World
        +Universe
    """.trimIndent()

        // when
        val result = GitDiffer.trimDiff(diffString)

        // then
        val expected = """
        --- a/file1.txt 
        +++ b/file1.txt 
        @@ -1,2 +1,2 @@
        -Hello
        +Hi
        -World
        +Universe
    """.trimIndent()

        expected shouldBe result
    }

}
