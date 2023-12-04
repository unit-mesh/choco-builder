package cc.unitmesh.processor.api.postman

import kotlinx.serialization.Serializable

@Serializable
class PostmanCollection {
    var info: PostmanInfo? = null
    var item: List<PostmanFolder>? = null
    var folderLookup: MutableMap<String?, PostmanFolder> = HashMap()
    var variable: List<PostmanVariable>? = null
    var auth: cc.unitmesh.processor.api.postman.PostmanAuth? = null
    var event: List<PostmanEvent>? = null

    fun init() {
        for (f in item!!) {
            folderLookup[f.name] = f
        }
    }
}
