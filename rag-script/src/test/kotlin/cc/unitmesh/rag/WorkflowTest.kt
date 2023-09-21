package cc.unitmesh.rag

import cc.unitmesh.cf.code.CodeSplitter
import cc.unitmesh.rag.document.Document
import chapi.domain.core.CodeDataStruct
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.Ignore

class WorkflowTest {

    @Test
    @Ignore
    fun index_and_query() {
        rag {
            val apiKey = env?.get("OPENAI_API_KEY") ?: ""
            val apiHost = env?.get("OPENAI_API_HOST") ?: ""

            llm = LlmConnector(LlmType.OpenAI, apiKey, apiHost)
            embedding = EmbeddingEngine(EngineType.SentenceTransformers)
            store = Store(StoreType.Elasticsearch)

            indexing {
                val cliUrl = "https://github.com/archguard/archguard/releases/download/v2.0.7/scanner_cli-2.0.7-all.jar"
                val file = Http.download(cliUrl)

                var outputFile = File("0_codes.json");
                if (!outputFile.exists()) {
                    outputFile = Exec().runJar(
                        file, args = listOf(
                            "--language", "Kotlin",
                            "--output", "json",
                            "--path", ".",
                            "--with-function-code"
                        )
                    ).also {
                        File("0_codes.json")
                    }
                }

                // todo: use dataframe to parse json
                val splitter = CodeSplitter()
                val chunks: List<Document> = Json.decodeFromString<List<CodeDataStruct>>(outputFile.readText())
                    .map(splitter::split).flatten()

                store.indexing(chunks)
            }

            querying {
                val results = store.findRelevant("workflow dsl design ")
                val sorted = results
                    .lowInMiddle()

                llm.completion {
                    """根据用户的问题，总结如下的代码
                        |${sorted.joinToString("\n") { "${it.score} ${it.embedded.text}" }}
                        |
                        |用户的问题是：如何设计一个 DSL 的 workflow
                    """.trimMargin()
                }.also {
                    println(it)
                }
            }
        }
    }

    @Test
    @Ignore
    fun index_and_query_simple() {
        rag("code") {
            llm = LlmConnector(LlmType.OpenAI)
            embedding = EmbeddingEngine(EngineType.SentenceTransformers)
            store = Store(StoreType.Elasticsearch)

            indexing {
                val cliUrl = "https://github.com/archguard/archguard/releases/download/v2.0.7/scanner_cli-2.0.7-all.jar"
                Exec().runJar(
                    Http.download(cliUrl), args = listOf(
                        "--language", "Kotlin",
                        "--output", "json",
                        "--path", ".",
                        "--with-function-code"
                    )
                )

                val chunks: List<Document> =
                    Json.decodeFromString<List<CodeDataStruct>>(File("0_codes.json").readText())
                        .map {
                            CodeSplitter().split(it)
                        }.flatten()

                store.indexing(chunks)
            }

            querying {
                val results = store.findRelevant("workflow dsl design ")
                val sorted = results.lowInMiddle()

                llm.completion {
                    """根据用户的问题，总结如下的代码
                        |${sorted.joinToString("\n") { "${it.score} ${it.embedded.text}" }}
                        |
                        |用户的问题是：如何设计一个 DSL 的 workflow
                    """.trimMargin()
                }.also {
                    println(it)
                }
            }
        }
    }

    @Test
    @Ignore
    fun document_handle() {
        rag("code") {
            // 使用 OpenAI 作为 LLM 引擎
            llm = LlmConnector(LlmType.OpenAI)
            // 使用 SentenceTransformers 作为 Embedding 引擎
            embedding = EmbeddingEngine(EngineType.SentenceTransformers)
            // 使用 Memory 作为 Retriever
            store = Store(StoreType.Memory)

            indexing {
                // 从文件中读取文档
                val document = document("filename.txt")
                // 将文档切割成 chunk
                val chunks = document.split()
                // 建立索引
                store.indexing(chunks)
            }

            querying {
                // 查询
                store.findRelevant("workflow dsl design ").also {
                    println(it)
                }
            }
        }
    }

    @Test
    fun template_example() {
        val promptText = prompt {
            paragraph("Hello World")
            codeblock("kotlin") {
                "println(\"Hello World\")"
            }
            list(ListType.Unordered) {
                listOf("Hello", "World")
            }
        }

        promptText.toString() shouldBe """Hello World
            |```kotlin
            |println("Hello World")
            |```
            |* Hello
            |* World
            |""".trimMargin()
    }
}
