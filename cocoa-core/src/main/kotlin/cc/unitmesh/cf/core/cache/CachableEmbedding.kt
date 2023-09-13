package cc.unitmesh.cf.core.cache

import cc.unitmesh.nlp.embedding.Embedding

public interface CachableEmbedding {
    fun create(text: String): Embedding
}