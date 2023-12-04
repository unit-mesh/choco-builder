package cc.unitmesh.processor.api.postman

import kotlinx.serialization.Serializable

@Serializable
class PostmanUrlEncoded {
    var key: String? = null
    var value: String? = null
    var type: String? = null
    var description: String? = null
    var disabled: Boolean? = null
}
