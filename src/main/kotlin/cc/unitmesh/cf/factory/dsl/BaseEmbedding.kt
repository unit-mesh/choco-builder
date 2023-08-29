package cc.unitmesh.cf.factory.dsl

import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding

interface BaseEmbedding {
    val id: String
    val embedding: Embedding
}
