package cc.unitmesh.rag.document

import cc.unitmesh.cf.core.llms.Embedding
import cc.unitmesh.cf.core.utils.IdUtil
import java.util.*


class Document {
    val id = IdUtil.uuid()
    var embedding: Embedding = listOf()
    var metadata: Map<String, Any> = HashMap()
    var text: String = ""
}

