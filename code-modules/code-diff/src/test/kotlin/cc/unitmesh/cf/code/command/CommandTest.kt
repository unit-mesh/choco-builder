package cc.unitmesh.cf.code.command

import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class CommandTest {
    @Test
    fun `test command manager git`() {
        var stdout = ""
        val options = ExecOptions(
            cwd = ".",
            listeners = object : ExecListeners {
                override fun stdout(data: String) {
                    stdout += data
                }

                override fun stderr(data: String) {
                    println(data)
                }
            }
        )

        Command().exec("git", listOf("--version"), options)
        stdout shouldNotBe ""
    }
}