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
        val docs = docGen.extractNodes(listOf(astNode))

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
        val docs = docGen.extractNodes(listOf(astNode))
        docs.size shouldBe 1
        docs[0].children.size shouldBe 1
    }

    @Test
    fun should_handle_the_sample_code() {
        val code = """
/**
 * Validator is an interface for validating result.
 */
interface Validator {
    val input: String
    fun validate(): Boolean
}

/**
 * JsonPath will validate is path is valid. If a path is invalid, will return false.
 */
class JsonPathValidator(val expression: String, override val input: String) : Validator {
    override fun validate(): Boolean = try {
        JsonPath.parse(input).read<Any>(expression)
        true
    } catch (e: Exception) {
        false
    }
}

class JsonPathValidatorTest {
    @Test
    @SampleCode(name = "检验成功", content = "")
    fun should_return_true_when_path_is_valid() {
        // start-sample
        val expression = "${'$'}.name"
        val input = "{\"name\": \"John\", \"age\": 30}"
        // end-sample

        // when
        val validator = JsonPathValidator(expression, input)
        val result = validator.validate()

        // then
        result shouldBe true
    }
}    
""".trimIndent()
        val processor = KtFileProcessor()
        val astNode = processor.process(Code.fromSnippet(code))
        val extractNodes = docGen.extractNodes(listOf(astNode))
        val element = extractNodes[0].children[0].element!!
        val codeSample = docGen.buildSample(element)!!

        codeSample.nodeName shouldBe "JsonPathValidator"
        val samples = codeSample.samples
        samples.size shouldBe  1
        samples[0].name shouldBe "检验成功"
        samples[0].content shouldBe ""
        samples[0].code shouldBe """val expression = "${'$'}.name"
val input = "{\"name\": \"John\", \"age\": 30}""""
    }
}
