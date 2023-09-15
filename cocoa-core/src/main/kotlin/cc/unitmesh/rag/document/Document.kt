package cc.unitmesh.rag.document

import cc.unitmesh.cf.core.utils.IdUtil
import cc.unitmesh.nlp.embedding.Embedding
import java.util.*


data class Document(
    val id: String = IdUtil.uuid(),
    var metadata: Metadata = HashMap(),
    var text: String = "",
    @Deprecated("This field is only used for ElasticSearch.")
    @Transient
    var vector: Embedding = emptyList()
) {
    constructor(text: String, metadata: Metadata = HashMap()) : this(
        IdUtil.uuid(),
        metadata,
        text
    )


    companion object {
        fun from(stringValue: String): Document {
            return Document(stringValue)
        }

        fun from(stringValue: String, metadata: Metadata): Document {
            return Document(stringValue, metadata)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Document

        if (metadata != other.metadata) return false
        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        var result = metadata.hashCode()
        result = 31 * result + text.hashCode()
        return result
    }
}

