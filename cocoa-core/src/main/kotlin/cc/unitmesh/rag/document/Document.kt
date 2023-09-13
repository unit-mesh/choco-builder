package cc.unitmesh.rag.document

import cc.unitmesh.cf.core.llms.Embedding
import cc.unitmesh.cf.core.utils.IdUtil
import java.util.*


data class Document(
    val id: String = IdUtil.uuid(),
    var embedding: Embedding = listOf(),
    var metadata: Map<String, Any> = HashMap(),
    var text: String = ""
) {
    constructor(text: String, metadata: Map<String, Any> = HashMap()) : this(
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
}

