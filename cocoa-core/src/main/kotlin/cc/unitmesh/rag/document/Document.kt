package cc.unitmesh.rag.document

import cc.unitmesh.cf.core.utils.IdUtil
import java.util.*


data class Document(
    val id: String = IdUtil.uuid(),
    var metadata: Metadata = HashMap(),
    var text: String = "",
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
    }
}

