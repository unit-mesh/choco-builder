package cc.unitmesh.prompt.executor;

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

        val scriptPath = this::class.java.getResource("/prompt.unit-mesh.yml")!!.toURI().path

        val scriptExecutor = ScriptExecutor(File(scriptPath))

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