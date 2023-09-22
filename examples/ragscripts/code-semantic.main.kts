@file:DependsOn("cc.unitmesh:rag-script:0.3.3")

import java.io.File
import cc.unitmesh.cf.code.CodeSplitter
import cc.unitmesh.rag.document.Document
import chapi.domain.core.CodeDataStruct
import cc.unitmesh.rag.*
import kotlinx.serialization.json.Json

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
        val chunks: List<Document> = Json.decodeFromString<List<CodeDataStruct>>(outputFile.readText()).map {
            CodeSplitter().split(it)
        }.flatten()

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

