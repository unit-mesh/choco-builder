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

## pgvector (TODO)

> Open-source vector similarity search for Postgres

