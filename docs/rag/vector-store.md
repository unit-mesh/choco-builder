---
layout: default
title: Vector Store
parent: Retrieval Augmented Generation
nav_order: 11
permalink: /rag/vector-store
---

{: .warning }
Automatically generated documentation; use the command `./gradlew :docs-builder:run` and update comments in the source code to reflect changes.

# EmbeddingStore 

> 向量数据库的核心是将数据表示为向量，并使用向量空间中的距离度量来进行数据的存储、检索和分析。

An interface for an Embedding Store, which is a vector database used to store and manage embeddings.
Embeddings are high-dimensional vector representations of data points, which can be used in various
machine learning and data retrieval tasks.



## ElasticsearchStore 

ElasticsearchStore is an implementation of the EmbeddingStore interface that uses Elasticsearch as the underlying storage.
It allows storing and retrieving embeddings along with associated documents.

The ElasticsearchStore class requires the following parameters to be provided:
- serverUrl: The URL of the Elasticsearch server. The default value is "http://localhost:9200".
- indexName: The name of the Elasticsearch index to use. The default value is "chocolate-code".
- username: The username for authentication with the Elasticsearch server. This parameter is optional.
- password: The password for authentication with the Elasticsearch server. This parameter is optional.
- apiKey: The API key for authentication with the Elasticsearch server. This parameter is optional.

The ElasticsearchStore class provides methods for adding embeddings and documents, as well as retrieving relevant embeddings based on a reference embedding.

```kotlin
val store: ElasticsearchStore = ElasticsearchStore(elasticsearchUrl)
```

## InMemoryEmbeddingStore 

The `InMemoryEmbeddingStore` class is an implementation of the `EmbeddingStore` interface that stores embeddings in memory.
It provides methods to add embeddings, retrieve relevant embeddings, and manage the storage of embeddings.

Sample: 

```kotlin
val embeddingStore: EmbeddingStore<Document> = InMemoryEmbeddingStore()

embeddingStore.add(toEmbedding(floatArrayOf(1f, 3f)), Document.from("first"))
embeddingStore.add(toEmbedding(floatArrayOf(2f, 2f)), Document.from("second"))

val relevant: List<EmbeddingMatch<Document>> =
    embeddingStore.findRelevant(toEmbedding(floatArrayOf(4f, 0f)), 2)
```

## InMemoryEnglishTextStore 

A simple in-memory English implementation of [EmbeddingStore].

This class represents an in-memory storage for English text embeddings. It implements the [EmbeddingStore] interface,
which provides methods for adding and retrieving embeddings.

The class stores the embeddings in a mutable list of [Entry] objects. Each entry contains an ID, an embedding, and an
optional embedded object. The ID is generated using the [IdUtil.uuid] method. The class provides multiple overloaded
methods for adding embeddings, allowing the user to specify the ID and the embedded object.

The class also provides methods for adding multiple embeddings at once. The [addAll] method takes a list of embeddings
and adds them to the store, returning a list of IDs for the added embeddings. There is also an overloaded version of
[addAll] that takes a list of embeddings and a list of embedded objects, ensuring that both lists have the same size.

The [findRelevant] method allows the user to find the most relevant embeddings in the store based on a reference
embedding. It takes the reference embedding, the maximum number of results to return, and the minimum relevance score
as parameters. It calculates the cosine similarity between the reference embedding and each entry in the store, and
filters the entries based on the minimum score. The method returns a list of [EmbeddingMatch] objects, sorted by
relevance score in descending order.



Sample: 

```kotlin
val store = InMemoryEnglishTextStore<String>()

store.add(provider.embed("this is a example"), "this is a example")
store.add(provider.embed("this is a dog"), "this is a dog")
store.add(provider.embed("this is item list"), "this is item list")

val maxResults = 1
val minScore = 0.5

val embedding4 = provider.embed("this is a cat")
```

