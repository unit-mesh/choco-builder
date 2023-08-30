package cc.unitmesh.cf.core.dsl

import cc.unitmesh.cf.infrastructure.llms.embedding.Embedding

interface EmbeddingElement: IndexElement {
    val embedding: Embedding
}
