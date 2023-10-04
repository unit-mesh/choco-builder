package cc.unitmesh.docs;

import com.pinterest.ktlint.Code
import com.pinterest.ktlint.KtFileProcessor
import org.junit.jupiter.api.Test
import java.nio.file.Path

class PromptScriptDocGenTest {
    val docGen = PromptScriptDocGen(Path.of("."))

    @Test
    fun should_handle_for_sealed_class_docs() {
        val processor = KtFileProcessor()
        val sourceCode =
"""
/**
 * StrategyItem is the job's strategy config.
 */
@Serializable
sealed class StrategyItem {
    /**
     * RepeatItem is a config of repeat times.
     * for example:
     * 
     *```yaml
     * - type: repeat
     *   value: 3
     *```
     */
    @SerialName("repeat")
    @Serializable
    data class RepeatItem(val value: Int) : StrategyItem()
}    
""".trimIndent()
        val astNode = processor.process(Code.fromSnippet(sourceCode))
        docGen.extractRootNode(astNode)
    }
}
