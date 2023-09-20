package cc.unitmesh.apply

import cc.unitmesh.cf.code.CodeSplitter
import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.rag.document.Document
import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class WorkflowTest {

    @Test
    @Ignore
    fun index_and_query() {
        apply("code") {
            connection = Connection(ConnectionType.OpenAI)
            embedding = EmbeddingEngine(EngineType.SentenceTransformers)
            vectorStore = VectorStore(StoreType.Elasticsearch)

            indexing {
                val cliUrl = "https://github.com/archguard/archguard/releases/download/v2.0.7/scanner_cli-2.0.7-all.jar"
                val file = Http.download(cliUrl)

                val outputFile = Exec("java -jar ${file.absolutePath} -h").run()?.also {
                    println(it.absolutePath)
                } ?: throw Exception("Cannot run the command")

                // todo: use dataframe to parse json
                val chunks: List<Document> = Json.decodeFromString<List<CodeDataStruct>>(outputFile.readText()).map {
                    CodeSplitter().split(it)
                }.flatten()

                vectorStore.indexing(chunks)
            }

            querying {
                val results = vectorStore.query("")
                val sorted = results
                    .lowInMiddle()

                connection.prompt {
                    """Some prompt in Here
                        |${sorted.joinToString("\n") { it.embedded.text }}
                    """.trimMargin()
                }.also {
                    println(it)
                }
            }
        }
    }

    @Test
    fun hello_apply() {
        apply("code") {
            connection = Connection(ConnectionType.OpenAI)

            prepare {
                val file =
                    Http.download("https://github.com/archguard/archguard/releases/download/v2.0.7/scanner_cli-2.0.7-all.jar") {

                    }

                val output = Exec("java -jar ${file.absolutePath} -h").run()
                // to json
                val chunks = document("filename").split()
                vectorStore().indexing(chunks)
            }
            problem {
                val dsl = {
                    // json schema ?
                }

                return@problem object : Dsl {
                    override var domain: String = ""
                    override val content: String = ""
                    override var interpreters: List<DslInterpreter> = listOf()
                }
            }
            solution {
                val results = vectorStore().query("")
                val sorted = results
                    .lowInMiddle()

                llm("openai").request {
                    // add prompt it here
                    """a"""
                }
            }
        }
    }
}
