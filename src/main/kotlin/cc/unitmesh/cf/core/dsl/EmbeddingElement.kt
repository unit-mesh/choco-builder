package cc.unitmesh.cf.core.dsl

import cc.unitmesh.cf.core.llms.Embedding

interface EmbeddingElement: IndexElement {
    val embedding: Embedding
}
