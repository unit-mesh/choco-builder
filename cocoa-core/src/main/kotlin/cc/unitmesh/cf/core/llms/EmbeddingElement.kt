package cc.unitmesh.cf.core.llms

import cc.unitmesh.cf.core.dsl.IndexElement

interface EmbeddingElement: IndexElement {
    val embedding: Embedding
}
