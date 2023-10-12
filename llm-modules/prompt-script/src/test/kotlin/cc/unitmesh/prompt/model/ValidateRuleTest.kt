package cc.unitmesh.prompt.model

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ValidateRuleTest {
    @Test
    fun should_serialize_from_ext_tool_with_options_yaml() {
        val yaml = """
            type: ext-tool
            value: dot -Tsvg basic.dot -o basic.svg
            options:
              key: test
              value: test
        """.trimIndent()

        val configuration = YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property)
        val validateRule = Yaml(configuration = configuration).decodeFromString(ValidateRule.serializer(), yaml)

        (validateRule is ValidateRule.ExtTool) shouldBe true
        val extTool = validateRule as ValidateRule.ExtTool
        extTool.value shouldBe "dot -Tsvg basic.dot -o basic.svg"
        extTool.options["key"] shouldBe "test"
    }
}