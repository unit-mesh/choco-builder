package cc.unitmesh.apply

import cc.unitmesh.cf.code.CodeSplitter
import cc.unitmesh.rag.document.Document
import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.Ignore

class WorkflowTest {

    @Test
    @Ignore
    fun index_and_query() {
        scripting("code") {
            connection = Connection(ConnectionType.OpenAI)
            embedding = EmbeddingEngine(EngineType.SentenceTransformers)
            vectorStore = VectorStore(StoreType.Elasticsearch)

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
                val chunks: List<Document> = Json.decodeFromString<List<CodeDataStruct>>(outputFile.readText()).map {
                    CodeSplitter().split(it)
                }.flatten()

                vectorStore.indexing(chunks)
            }

            querying {
                val results = vectorStore.query("workflow dsl design ")
                val sorted = results
                    .lowInMiddle()

                connection.completion {
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
        scripting("code") {
            connection = Connection(ConnectionType.OpenAI)
            embedding = EmbeddingEngine(EngineType.SentenceTransformers)
            vectorStore = VectorStore(StoreType.Elasticsearch)

            indexing {
                val cliUrl = "https://github.com/archguard/archguard/releases/download/v2.0.7/scanner_cli-2.0.7-all.jar"

                val outputFile = Exec().runJar(
                    Http.download(cliUrl),
                    args = listOf(
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

                vectorStore.indexing(chunks)
            }

            querying {
                val results = vectorStore.query("workflow dsl design ")
                val sorted = results.lowInMiddle()

                connection.completion {
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
}
