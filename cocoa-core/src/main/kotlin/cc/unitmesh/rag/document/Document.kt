package cc.unitmesh.rag.document

import cc.unitmesh.nlp.embedding.Embedding
import cc.unitmesh.cf.core.utils.IdUtil
import java.util.*


data class Document(
    val id: String = IdUtil.uuid(),
    var embedding: Embedding = listOf(),
    var metadata: Metadata = HashMap(),
    var text: String = "",
) {
    constructor(text: String, metadata: Metadata = HashMap()) : this(
        IdUtil.uuid(),
        listOf(),
        metadata,
        text
    )

    // equals ignore id
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Document) {
            return false
        }

        return this.text == other.text && this.metadata == other.metadata
    }

    override fun hashCode(): Int {
        return Objects.hash(text, metadata)
    }
}

