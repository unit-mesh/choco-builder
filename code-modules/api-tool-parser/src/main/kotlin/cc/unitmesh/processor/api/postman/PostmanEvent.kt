package cc.unitmesh.processor.api.postman

import kotlinx.serialization.Serializable

@Serializable
class PostmanEvent {
    var listen: String? = null
    var script: PostmanScript? = null
}
