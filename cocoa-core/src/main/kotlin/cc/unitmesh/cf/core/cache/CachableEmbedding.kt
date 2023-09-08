package cc.unitmesh.cf.core.cache

import cc.unitmesh.cf.core.llms.Embedding

public interface CachableEmbedding {
    fun create(text: String): Embedding
}