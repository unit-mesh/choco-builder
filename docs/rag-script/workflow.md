---
layout: default
title: Workflow
parent: RAG Script
nav_order: 10
permalink: /rag-script/workflow
---

{: .warning }
Automatically generated documentation; use the command `./gradlew :docs-builder:run` and update comments in the source code to reflect changes.

# Workflow 

> RAGScript 是一个使用 Kotlin DSL 的脚本语言，以用于快速使用、构建 RAG （检索增强，Retrieval Augmented Generation）应用的 PoC。

适用场景：安装有 Intellij IDEA、Kotlin Jupyter 环境或者 Kotlin 编译器的开发环境。

Sample: 代码语义化搜索

```kotlin
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
            outputFile = Exec.runJar(
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
```

Sample: 最简洁的 RAG 示例

```kotlin
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
        store.findRelevant("workflow dsl design ").lowInMiddle().also {
            println(it)
        }
    }
}
```

Sample: 最短 RAG 示例

```kotlin
rag {
    indexing {
        val chunks = text("fun main(args: Array<String>) {\n    println(\"Hello, World!\")\n}").split()
        store.indexing(chunks)
    }

    querying {
        store.findRelevant("Hello World").also {
            println(it)
        }
    }
}
```

## document 

`document` function for provide document split for indexing, will auto-detect a file type.
support: txt, pdf, doc, docx, xls, xlsx, ppt, pptx

## directory 

Directory is a function for indexing data for the workflow.

## code 

TODO: `code` function for provide code split for indexing.

## text 

`text` function for provide text split for indexing.

## prepare 

Prepare is a function for preparing data for the workflow. You don't need to call it as block.

## indexing 

Indexing is a function for indexing data for the workflow. You don't need to call it as block.

## querying 

Querying is a function for querying data for the workflow. You don't need to call it as block.

## problem 

Problem space is a function for defining the problem.

## solution 

Solution space is a function for defining the solution.

## step 

Step is for tagging function block only.

