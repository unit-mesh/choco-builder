package cc.unitmesh.cf.code;

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CodeSplitterTest {

    @Test
    fun should_splitCodeDataStructIntoDocuments() {
        // Given
        val codeDataStruct = CodeDataStruct(
            Package = "com.example",
            NodeName = "MyClass",
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
        assertThat(documents).hasSize(2)
        assertThat(documents[0].text).isEqualTo("// canonicalName: com.example.MyClass\nfun foo() {\n    println(\"Hello, World!\")\n}")
        assertThat(documents[1].text).isEqualTo("// canonicalName: com.example.MyClass\nfun bar() {\n    println(\"Goodbye, World!\")\n}")
    }

    @Test
    fun should_splitCodeFunctionIntoDocument() {
        // Given
        val codeFunction = CodeFunction(
            Content = "fun foo() {\n    println(\"Hello, World!\")\n}",
        )

        val codeSplitter = CodeSplitter()

        // When
        val document = codeSplitter.split(codeFunction, "// canonicalName: com.example.MyClass")

        // Then
        assertThat(document.text).isEqualTo("// canonicalName: com.example.MyClass\nfun foo() {\n    println(\"Hello, World!\")\n}")
    }
}