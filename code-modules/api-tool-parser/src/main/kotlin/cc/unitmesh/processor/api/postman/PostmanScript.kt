package cc.unitmesh.processor.api.postman

import kotlinx.serialization.Serializable

@Serializable
class PostmanScript {
    var id: String? = null
    var type: String? = null
    var exec: List<String>? = null
}
