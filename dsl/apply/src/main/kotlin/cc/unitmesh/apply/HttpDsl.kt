package cc.unitmesh.apply

import java.io.File

class HttpDsl() {
    fun get(url: String = "", init: HttpDsl.() -> Any) {

    }
}

object Http {
    fun get(url: String = "", init: HttpDsl.() -> Unit) {

    }

    fun download(url: String, function: () -> Unit): File {
        val tempFile = File.createTempFile("download", "tmp")
        function()
        return tempFile
    }
}
