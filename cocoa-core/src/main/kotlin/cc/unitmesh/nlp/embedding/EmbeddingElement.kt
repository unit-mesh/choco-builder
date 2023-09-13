package cc.unitmesh.nlp.embedding

import cc.unitmesh.cf.core.dsl.IndexElement

interface EmbeddingElement: IndexElement {
    val embedding: Embedding
}
