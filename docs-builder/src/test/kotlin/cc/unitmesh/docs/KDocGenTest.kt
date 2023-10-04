package cc.unitmesh.docs;

import com.pinterest.ktlint.Code
import com.pinterest.ktlint.KtFileProcessor
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.nio.file.Path

class KDocGenTest {
    private val docGen = KDocGen(Path.of("."))

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
     * ConnectionItem is a config of [cc.unitmesh.connection.BaseConnection],
     * which will be used for [cc.unitmesh.openai.LlmProvider]
     * like temperature, top-p, top-k, presence_penalty, frequency_penalty, stop etc.
     * for example:
     *
     *```yaml
     * - type: connection
     *   value:
     *     - type: range
     *       key: temperature
     *       range: 0.7~1.0
     *       step: 0.1
     *```
     *
     */
    @SerialName("connection")
    @Serializable
    data class ConnectionItem(val value: List<Variable>) : StrategyItem()

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
        val docs = docGen.extractRootNode(astNode)

        docs.size shouldBe 1
        docs[0].children.size shouldBe 2
    }

    @Test
    fun should_handle_for_interface() {
        val code = """
/**
 * Semantic is a interface for embedding text.
 */               
interface Semantic {
    fun embed(input: String): List<Double>
}

/**
 * STSemantic is a implementation of [Semantic] for Demo
 */
class STSemantic(): Semantic {
    override fun embed(input: String): List<Double> {
        return listOf()
    }
}

""".trimIndent()

        val processor = KtFileProcessor()
        val astNode = processor.process(Code.fromSnippet(code))
        val normalDocs = docGen.extractRootNode(astNode)
        normalDocs.size shouldBe 0

        val interfaceDocs = docGen.buildInterfaceDocs()
        interfaceDocs.size shouldBe 1
        interfaceDocs[0].children.size shouldBe 1
    }
}
