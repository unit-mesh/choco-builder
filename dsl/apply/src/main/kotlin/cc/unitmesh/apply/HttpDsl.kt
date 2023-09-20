package cc.unitmesh.apply

class HttpDsl() {
    fun get(url: String = "", init: HttpDsl.() -> Any) {

    }
}

object http {
    fun get(url: String = "", init: HttpDsl.() -> Unit) {

    }
}
