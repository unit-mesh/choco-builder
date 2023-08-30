package cc.unitmesh.cf.core.dsl

import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding

interface BaseEmbedding {
    val id: String
    val embedding: Embedding
}
