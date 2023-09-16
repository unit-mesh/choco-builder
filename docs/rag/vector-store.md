---
layout: default
title: Vector Store
parent: Retrieval Augmented Generation
nav_order: 3
---

## InMemoryEmbeddingStore

```kotlin
// 1. initialize the vector store
val vectorStore: EmbeddingStore<Document> = InMemoryEmbeddingStore()
// 2. add the embeddings
vectorStore.addAll(embeddings, documentList)

// 3. retrieves the similar documents
val vectorStoreRetriever = EmbeddingStoreRetriever(vectorStore)
val similarDocuments: List<EmbeddingMatch<Document>> = vectorStoreRetriever.retrieve(userQuery)
```

## Elasticsearch

Elasticsearch 可以提供向量搜索，以及普通的文本搜索，可以作为代码搜索的后端场景。

实现类：ElasticsearchStore，基于 LangChain4j 的代码，实现了一个 Elasticsearch 的存储引擎。

```kotlin
@Throws(JsonProcessingException::class)
private fun buildDefaultScriptScoreQuery(vector: Embedding, minScore: Float): ScriptScoreQuery {
    val queryVector = toJsonData(vector)
    return ScriptScoreQuery.of { q: ScriptScoreQuery.Builder ->
        q
            .minScore(minScore)
            .query(Query.of { qu: Query.Builder -> qu.matchAll { m: MatchAllQuery.Builder? -> m } })
            .script { s: Script.Builder ->
                s.inline(InlineScript.of { i: InlineScript.Builder ->
                    i // The script adds 1.0 to the cosine similarity to prevent the score from being negative.
                        // divided by 2 to keep the score in the range [0, 1]
                        .source("(cosineSimilarity(params.query_vector, 'vector') + 1.0) / 2")
                        .params("query_vector", queryVector)
                })
            }
    }
}
```

## pgvector (TODO)

> Open-source vector similarity search for Postgres

