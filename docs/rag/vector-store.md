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

