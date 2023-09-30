package cc.unitmesh.prompt.executor;

import cc.unitmesh.connection.OpenAiConnection
import cc.unitmesh.openai.OpenAiProvider
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class ScriptExecutorTest {

    // 在测试方法之前初始化 Mockk
    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun should_execute_script_successfully() {
        mockkStatic(File::readBytes)
        mockkStatic(File::readText)
        mockkStatic(File::writeText)

        val scriptContent = this::class.java.getResource("/prompt.unit-mesh.yml")!!.readText()

        val scriptExecutor = ScriptExecutor(scriptContent)

        val slot = slot<File>()

        val connectionContent = this::class.java.getResource("/connection/prompt-connection.yml")!!.readText()
        every { capture(slot).readBytes() } returns connectionContent.toByteArray()
        every { capture(slot).readText() } answers {
            this::class.java.getResource("/testdata/sample.json")!!.readText()
        }

        // mock write to file
        every { any<File>().writeText(any()) } just Runs

        // when
        scriptExecutor.execute()

        // then

        // 清除所有 Mockk 调用
        clearAllMocks()

    }
}