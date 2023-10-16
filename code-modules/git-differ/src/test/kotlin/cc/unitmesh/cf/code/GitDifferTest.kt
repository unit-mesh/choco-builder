package cc.unitmesh.cf.code;

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class GitDifferTest {
    @Test
    fun should_trim_diff_string() {
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
