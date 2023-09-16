package cc.unitmesh.cf.code;

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import io.kotest.matchers.shouldBe
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CodeSplitterTest {

    @Test
    fun should_use_ds_content() {
        // Given
        val content =
            "class MyClass {\n    fun foo() {\n        println(\"Hello, World!\")\n    }\n    fun bar() {\n        println(\"Goodbye, World!\")\n    }\n}"
        val codeDataStruct = CodeDataStruct(
            Package = "com.example",
            NodeName = "MyClass",
            Content = content,
            Functions = listOf(
                CodeFunction(
                    Content = "fun foo() {\n    println(\"Hello, World!\")\n}",
                ),
                CodeFunction(
                    Content = "fun bar() {\n    println(\"Goodbye, World!\")\n}",
                )
            )
        )

        val codeSplitter = CodeSplitter()

        // When
        val documents = codeSplitter.split(codeDataStruct)

        // Then
        documents.size shouldBe 1
        documents[0].text shouldBe "// canonicalName: com.example.MyClass\n$content"
    }

    @Test
    fun should_split_to_function() {
        // Given
        val codeFunction = CodeFunction(Content = "fun foo() {\n    println(\"Hello, World!\")\n}")

        val codeSplitter = CodeSplitter()

        // When
        val document = codeSplitter.split(codeFunction, "// canonicalName: com.example.MyClass")

        // Then
        document.text shouldBe "// canonicalName: com.example.MyClass\nfun foo() {\n    println(\"Hello, World!\")\n}"
    }
}