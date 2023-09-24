---
layout: default
title: RAGScript
parent: Retrieval Augmented Generation
nav_order: 99
---

RAGScript 是一个使用 Kotlin DSL 的脚本语言，以用于快速使用、构建 RAG （检索增强，Retrieval Augmented Generation）应用的 PoC。

适用场景：安装有 Intellij IDEA、Kotlin Jupyter 环境或者 Kotlin 编译器的开发环境

需知：文件名必须以 `*.main.kts` 的形式命名，否则无法运行。

示例：

```kotlin
// 声明依赖
@file:DependsOn("cc.unitmesh:rag-script:0.3.3")

// 引入 RAGScript 依赖
import cc.unitmesh.rag.*

rag {
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
```

## 相似性搜索代码示例

```kotlin
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
        // Data Loader
        val cliUrl = "https://github.com/archguard/archguard/releases/download/v2.0.7/scanner_cli-2.0.7-all.jar"
        val file = Http.download(cliUrl)
        Exec().runJar(
            file, args = listOf(
                "--language", "Kotlin",
                "--output", "json",
                "--path", ".",
                "--with-function-code"
            )
        )

        // Code Splitter
        val chunks: List<Document> = Json.decodeFromString<List<CodeDataStruct>>(File("0_codes.json").readText()).map {
            CodeSplitter().split(it)
        }.flatten()

        store.indexing(chunks)
    }

    querying {
        val results = store.findRelevant("workflow dsl design ")
        val sorted = results
            .lowInMiddle()

        llm.completion {
            """Your job is to answer a query about a codebase using the information above.
            |...
            |相关的代码如下：
            |${sorted.joinToString("\n") { "${it.score} ${it.embedded.text}" }}
            |
            |用户的问题是：如何设计一个 DSL 的 workflow
            |""".trimMargin()
        }.also {
            println(it)
        }
    }
}
```